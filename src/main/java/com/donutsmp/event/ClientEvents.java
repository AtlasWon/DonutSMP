package com.donutsmp.event;

import com.donutsmp.config.DonutConfig;
import com.donutsmp.render.DonutLoadingScreen;
import com.donutsmp.render.Draw;
import com.donutsmp.render.Particles;
import com.donutsmp.screen.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {

    private static final Particles uniParticles = new Particles();
    private static int upW, upH;
    private static long uniT0 = System.currentTimeMillis();
    private static boolean reRendering = false;

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent e) {
        if (!DonutConfig.ENABLED.get()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null && mc.screen.getClass() == TitleScreen.class)
            mc.setScreen(new DonutTitleScreen());
    }

    @SubscribeEvent
    public static void onOpen(ScreenEvent.Opening e) {
        if (!DonutConfig.ENABLED.get()) return;
        Screen s = e.getNewScreen();
        Minecraft mc = Minecraft.getInstance();
        if (s.getClass() == TitleScreen.class) { e.setNewScreen(new DonutTitleScreen()); return; }
        if (s.getClass() == SelectWorldScreen.class) { e.setNewScreen(new OWorldScreen(getParent())); return; }
        if (s.getClass() == JoinMultiplayerScreen.class) { e.setNewScreen(new OMultiScreen(getParent())); return; }
        if (s.getClass() == OptionsScreen.class) { e.setNewScreen(new OOptionsScreen(getParent(), mc.options)); return; }
    }

    @SubscribeEvent
    public static void onRenderPost(ScreenEvent.Render.Post e) {
        if (!DonutConfig.ENABLED.get() || reRendering) return;
        Screen s = e.getScreen();
        int w = s.width, h = s.height;
        if (w <= 0 || h <= 0) return;
        GuiGraphics g = e.getGuiGraphics();
        int mx = e.getMouseX(), my = e.getMouseY();
        float pt = e.getPartialTick();
        if (isLoading(s)) { DonutLoadingScreen.render(g, s, w, h, pt); return; }
        if (s instanceof DonutTitleScreen || s instanceof OWorldScreen
                || s instanceof OMultiScreen || s instanceof OOptionsScreen
                || s instanceof BgMenuScreen) return;
        if (Minecraft.getInstance().level == null) {
            renderUniversal(g, s, w, h, mx, my, pt);
        }
    }

    private static void renderUniversal(GuiGraphics g, Screen s, int w, int h, int mx, int my, float pt) {
        if (w != upW || h != upH) { uniParticles.init(w, h); upW = w; upH = h; }
        float time = (float)(System.currentTimeMillis() - uniT0);
        Draw.background(g, w, h, time);
        uniParticles.tick();
        uniParticles.render(g);
        reRendering = true;
        try {
            for (GuiEventListener child : s.children()) {
                if (child instanceof Renderable r) {
                    r.render(g, mx, my, pt);
                }
            }
        } catch (Exception ignored) {}
        reRendering = false;
        if (s.getTitle() != null) {
            String title = s.getTitle().getString();
            if (!title.isEmpty()) {
                Draw.text(g, Minecraft.getInstance().font, title, w / 2, 8,
                        DonutConfig.wa(DonutConfig.BRIGHT, 240));
            }
        }
    }

    private static boolean isLoading(Screen s) {
        if (s instanceof LevelLoadingScreen || s instanceof ReceivingLevelScreen || s instanceof ConnectScreen) return true;
        if (s instanceof GenericMessageScreen) {
            String t = s.getTitle() != null ? s.getTitle().getString().toLowerCase() : "";
            return t.contains("loading") || t.contains("saving") || t.contains("connecting")
                    || t.contains("building") || t.contains("generating") || t.contains("terrain")
                    || t.contains("preparing") || t.contains("downloading");
        }
        return false;
    }

    private static Screen getParent() {
        Minecraft mc = Minecraft.getInstance();
        return mc.screen instanceof DonutTitleScreen ? mc.screen : null;
    }
}
