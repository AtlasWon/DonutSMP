package com.donutsmp.render;

import com.donutsmp.DonutSMP;
import com.donutsmp.config.DonutConfig;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class CustomBg {

    private static ResourceLocation staticTex = null;
    private static int imgW = 0, imgH = 0;
    private static final List<ResourceLocation> gifFrames = new ArrayList<>();
    private static int gifW = 0, gifH = 0;
    private static boolean loaded = false;
    private static boolean isGif = false;
    private static boolean triedConfigLoad = false;
    private static int texCounter = 0;

    public static boolean isActive() {
        if (!triedConfigLoad) {
            triedConfigLoad = true;
            try {
                String path = DonutConfig.CUSTOM_BG_PATH.get();
                if (path != null && !path.isEmpty() && Files.exists(Path.of(path))) {
                    Minecraft.getInstance().execute(() -> loadFile(path));
                }
            } catch (Exception e) {
                DonutSMP.LOGGER.error("[DonutSMP] Config bg load error", e);
            }
        }
        return loaded;
    }

    public static void render(GuiGraphics g, int sw, int sh) {
        if (!loaded) return;
        g.fill(0, 0, sw, sh, 0xFF000000);
        try {
            if (isGif && !gifFrames.isEmpty()) {
                int idx = (int)((System.currentTimeMillis() / 100) % gifFrames.size());
                g.blit(gifFrames.get(idx), 0, 0, sw, sh, 0, 0, gifW, gifH, gifW, gifH);
            } else if (staticTex != null) {
                g.blit(staticTex, 0, 0, sw, sh, 0, 0, imgW, imgH, imgW, imgH);
            }
        } catch (Exception e) {
            // Silently handle render errors
        }
        g.fill(0, 0, sw, sh, DonutConfig.argb(40, 0, 0, 0));
    }

    /** Opens a Swing file chooser for images. Uses JFileChooser which works everywhere. */
    public static void pickImage() {
        DonutSMP.LOGGER.info("[DonutSMP] Opening image picker...");
        new Thread(() -> {
            try {
                // Must set look-and-feel on the thread that creates the dialog
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choose Background Image");
                chooser.setFileFilter(new FileNameExtensionFilter("Images (PNG, JPG, BMP)", "png", "jpg", "jpeg", "bmp"));
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    DonutSMP.LOGGER.info("[DonutSMP] Image selected: {}", path);
                    Minecraft.getInstance().execute(() -> loadFile(path));
                }
            } catch (Exception e) {
                DonutSMP.LOGGER.error("[DonutSMP] Image picker failed", e);
            }
        }, "DonutSMP-ImagePicker").start();
    }

    /** Opens a Swing file chooser for GIFs. */
    public static void pickVideo() {
        DonutSMP.LOGGER.info("[DonutSMP] Opening GIF picker...");
        new Thread(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choose Background GIF");
                chooser.setFileFilter(new FileNameExtensionFilter("GIF Animations", "gif"));
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    DonutSMP.LOGGER.info("[DonutSMP] GIF selected: {}", path);
                    Minecraft.getInstance().execute(() -> loadFile(path));
                }
            } catch (Exception e) {
                DonutSMP.LOGGER.error("[DonutSMP] GIF picker failed", e);
            }
        }, "DonutSMP-GIFPicker").start();
    }

    public static void reset() {
        releaseAll();
        loaded = false;
        isGif = false;
        try { DonutConfig.CUSTOM_BG_PATH.set(""); } catch (Exception ignored) {}
        DonutSMP.LOGGER.info("[DonutSMP] Background reset to default.");
    }

    private static void loadFile(String path) {
        try {
            DonutSMP.LOGGER.info("[DonutSMP] Loading background from: {}", path);
            if (!Files.exists(Path.of(path))) {
                DonutSMP.LOGGER.error("[DonutSMP] File not found: {}", path);
                return;
            }
            releaseAll();
            if (path.toLowerCase().endsWith(".gif")) {
                loadGif(path);
            } else {
                loadImage(path);
            }
            if (loaded) {
                try { DonutConfig.CUSTOM_BG_PATH.set(path); } catch (Exception ignored) {}
                DonutSMP.LOGGER.info("[DonutSMP] Background loaded successfully!");
            }
        } catch (Exception e) {
            DonutSMP.LOGGER.error("[DonutSMP] Failed to load background", e);
            loaded = false;
        }
    }

    /**
     * Load any image format using Java ImageIO, then convert to NativeImage.
     * This supports PNG, JPG, BMP, WBMP, and GIF (static).
     */
    private static void loadImage(String path) throws Exception {
        BufferedImage bi = ImageIO.read(new File(path));
        if (bi == null) {
            DonutSMP.LOGGER.error("[DonutSMP] ImageIO.read returned null for: {}", path);
            return;
        }
        imgW = bi.getWidth();
        imgH = bi.getHeight();
        NativeImage ni = bufferedToNative(bi);
        DynamicTexture dt = new DynamicTexture(ni);
        staticTex = Minecraft.getInstance().getTextureManager()
                .register("donutsmp_bg_" + (texCounter++), dt);
        isGif = false;
        loaded = true;
        DonutSMP.LOGGER.info("[DonutSMP] Static image loaded: {}x{}", imgW, imgH);
    }

    private static void loadGif(String path) throws Exception {
        ImageInputStream iis = ImageIO.createImageInputStream(new File(path));
        java.util.Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
        if (!readers.hasNext()) {
            iis.close();
            loadImage(path);
            return;
        }
        ImageReader reader = readers.next();
        reader.setInput(iis);
        int numFrames = reader.getNumImages(true);
        if (numFrames <= 0) { reader.dispose(); iis.close(); loadImage(path); return; }

        int max = Math.min(numFrames, 150);
        for (int i = 0; i < max; i++) {
            BufferedImage bi = reader.read(i);
            if (i == 0) { gifW = bi.getWidth(); gifH = bi.getHeight(); }
            NativeImage ni = bufferedToNative(bi);
            DynamicTexture dt = new DynamicTexture(ni);
            ResourceLocation loc = Minecraft.getInstance().getTextureManager()
                    .register("donutsmp_gif_" + (texCounter++) + "_" + i, dt);
            gifFrames.add(loc);
        }
        reader.dispose();
        iis.close();
        isGif = true;
        loaded = true;
        DonutSMP.LOGGER.info("[DonutSMP] GIF loaded: {}x{}, {} frames", gifW, gifH, gifFrames.size());
    }

    /** Convert BufferedImage to NativeImage pixel by pixel. Handles ABGR format. */
    private static NativeImage bufferedToNative(BufferedImage bi) {
        // Convert to ARGB first
        BufferedImage argbImg = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
        argbImg.getGraphics().drawImage(bi, 0, 0, null);

        NativeImage ni = new NativeImage(argbImg.getWidth(), argbImg.getHeight(), false);
        for (int y = 0; y < argbImg.getHeight(); y++) {
            for (int x = 0; x < argbImg.getWidth(); x++) {
                int argb = argbImg.getRGB(x, y);
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;
                // NativeImage uses ABGR pixel format
                ni.setPixelRGBA(x, y, (a << 24) | (b << 16) | (g << 8) | r);
            }
        }
        return ni;
    }

    private static void releaseAll() {
        Minecraft mc = Minecraft.getInstance();
        if (staticTex != null) { try { mc.getTextureManager().release(staticTex); } catch (Exception ignored) {} staticTex = null; }
        for (ResourceLocation loc : gifFrames) { try { mc.getTextureManager().release(loc); } catch (Exception ignored) {} }
        gifFrames.clear();
        imgW = imgH = gifW = gifH = 0;
    }
}
