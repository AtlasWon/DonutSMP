package com.donutsmp.render;

import com.donutsmp.config.DonutConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.*;

public class DonutLoadingScreen {
    private static final Particles particles = new Particles();
    private static int lw,lh;
    private static long t0;
    private static String lastCls="",tip="";
    private static final String[] TIPS={"Endermen can't pick up obsidian.","Honey blocks reduce fall damage by 80%.",
        "Dolphins boost your swim speed.","Netherite items float in lava.","Cats scare away Creepers and Phantoms.",
        "A full beacon needs 164 mineral blocks.","Axolotls help fight underwater mobs.","Lightning rods prevent fires.",
        "Piglins love gold armor.","Sculk sensors detect vibrations!"};

    public static void render(GuiGraphics g, Screen scr, int w, int h, float pt) {
        String cls=scr.getClass().getName();
        if(!cls.equals(lastCls)){lastCls=cls;t0=System.currentTimeMillis();tip=TIPS[(int)(Math.random()*TIPS.length)];}
        if(w!=lw||h!=lh){particles.init(w,h);lw=w;lh=h;}
        float time=(float)(System.currentTimeMillis()-t0);
        Font f=Minecraft.getInstance().font;
        int cx=w/2;

        // BG
        Draw.background(g,w,h,time);
        particles.tick(); particles.render(g);

        // CARD - taller for better spacing
        int cw=300, ch=130;
        int cX=cx-cw/2, cY=h/2-ch/2;
        Draw.card(g,cX,cY,cw,ch,DonutConfig.PURPLE,time);

        // TITLE at y=cY+18
        String title=getTitle(scr);
        Draw.glowText(g,f,title,cx,cY+18,DonutConfig.wa(DonutConfig.PURPLE,255));

        // PROGRESS BAR at y=cY+44 — FILLS TO 100%
        int barW=cw-50, barH=6, barX=cX+25, barY=cY+44;

        // Smooth fill: starts fast, reaches 100% around 15 seconds
        float elapsed = time / 1000f; // seconds
        float progress;
        if (elapsed < 12f) {
            progress = elapsed / 12f; // linear to ~100% over 12 sec
        } else {
            progress = 1.0f; // fully filled
        }
        progress = Math.max(0, Math.min(1.0f, progress));

        // Track
        g.fill(barX,barY,barX+barW,barY+barH, DonutConfig.argb(200,8,8,18));

        // Fill
        int fillW=(int)(progress*(barW-2));
        if(fillW>0){
            int fr=(int)(DonutConfig.r(DonutConfig.PURPLE)*(1-progress)+DonutConfig.r(DonutConfig.CYAN)*progress);
            int fg=(int)(DonutConfig.g(DonutConfig.PURPLE)*(1-progress)+DonutConfig.g(DonutConfig.CYAN)*progress);
            int fb=(int)(DonutConfig.b(DonutConfig.PURPLE)*(1-progress)+DonutConfig.b(DonutConfig.CYAN)*progress);
            // Main fill
            g.fill(barX+1,barY+1,barX+1+fillW,barY+barH-1, DonutConfig.argb(220,fr,fg,fb));
            // Bright edge
            if(fillW>3){int eX=barX+fillW-1;
                g.fill(eX,barY+1,eX+2,barY+barH-1, DonutConfig.argb(255,Math.min(255,fr+80),Math.min(255,fg+80),Math.min(255,fb+80)));}
            // Top highlight
            g.fill(barX+1,barY+1,barX+1+fillW,barY+2, DonutConfig.argb(35,255,255,255));
        }
        // Border
        int bbc=DonutConfig.wa(DonutConfig.BORDER,80);
        g.fill(barX-1,barY-1,barX+barW+1,barY,bbc);g.fill(barX-1,barY+barH,barX+barW+1,barY+barH+1,bbc);
        g.fill(barX-1,barY,barX,barY+barH,bbc);g.fill(barX+barW,barY,barX+barW+1,barY+barH,bbc);

        // Percentage at y=cY+44 (right of bar)
        String pct=String.format("%.0f%%",progress*100);
        g.drawString(f,pct,barX+barW+8,barY-1,DonutConfig.wa(DonutConfig.MID,220),false);

        // STATUS at y=cY+66
        int dp=(int)((time/400)%4);StringBuilder d=new StringBuilder("Please wait");
        for(int i=0;i<3;i++)d.append(i<dp?".":" ");
        Draw.text(g,f,d.toString(),cx,cY+66,DonutConfig.wa(DonutConfig.MID,200));

        // SPINNER at y=cY+92
        Draw.spinner(g,cx,cY+92,time,DonutConfig.CYAN);

        // TIP well below card at y=h-24
        Draw.text(g,f,tip,cx,h-24,DonutConfig.wa(DonutConfig.DIM,160));
    }

    private static String getTitle(Screen s){
        if(s instanceof LevelLoadingScreen)return "L O A D I N G   W O R L D";
        if(s instanceof ReceivingLevelScreen)return "R E C E I V I N G   D A T A";
        if(s instanceof ConnectScreen)return "C O N N E C T I N G";
        if(s.getTitle()!=null){String t=s.getTitle().getString();if(t!=null&&!t.isEmpty())return t.toUpperCase();}
        return "L O A D I N G";
    }
}
