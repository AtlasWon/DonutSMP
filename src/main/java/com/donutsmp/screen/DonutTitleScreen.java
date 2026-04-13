package com.donutsmp.screen;

import com.donutsmp.config.DonutConfig;
import com.donutsmp.render.*;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DonutTitleScreen extends Screen {
    private final Particles particles=new Particles();
    private long t0=System.currentTimeMillis();
    private static final int BW=240,BH=24,BG=30;
    private static final String[] TAGS={"The Sweetest Client","Powered by Donuts","Your World. Elevated.",
            "Sprinkle Some Style","Glazed to Perfection","Extra Frosting"};
    private final String tag=TAGS[(int)(Math.random()*TAGS.length)];

    public DonutTitleScreen(){super(Component.literal("DonutSMP"));}

    @Override protected void init(){super.init();particles.init(width,height);t0=System.currentTimeMillis();
        int cx=width/2,by=height/2+15;
        addRenderableWidget(new OButton(cx-BW/2,by,BW,BH,Component.literal("\u25B6  SINGLEPLAYER"),
                b->minecraft.setScreen(new OWorldScreen(this)),DonutConfig.PURPLE));
        addRenderableWidget(new OButton(cx-BW/2,by+BG,BW,BH,Component.literal("\u25C6  MULTIPLAYER"),
                b->minecraft.setScreen(new OMultiScreen(this)),DonutConfig.CYAN));
        int hw=BW/2-3;
        addRenderableWidget(new OButton(cx-BW/2,by+BG*3,hw,BH,Component.literal("\u2699  OPTIONS"),
                b->minecraft.setScreen(new OOptionsScreen(this,minecraft.options)),DonutConfig.PINK));
        addRenderableWidget(new OButton(cx+3,by+BG*3,hw,BH,Component.literal("\u2716  QUIT"),
                b->minecraft.stop(),DonutConfig.PINK));
        addRenderableWidget(new OButton(6,height-28,80,20,Component.literal("\u25A3 Theme"),
                b->minecraft.setScreen(new BgMenuScreen(this)),DonutConfig.GOLD));
    }

    @Override public void renderBackground(GuiGraphics g,int mx,int my,float pt){
        float time=(float)(System.currentTimeMillis()-t0);Draw.background(g,width,height,time);particles.tick();particles.render(g);}

    @Override public void render(GuiGraphics g,int mx,int my,float pt){super.render(g,mx,my,pt);
        float time=(float)(System.currentTimeMillis()-t0);Font f=this.font;int cx=width/2;
        int ty=height/2-70;Draw.card(g,cx-140,ty-24,280,88,DonutConfig.PURPLE,time);
        int dc=DonutConfig.wa(DonutConfig.PINK,240);g.fill(cx-5,ty-14,cx+6,ty-13,dc);g.fill(cx,ty-19,cx+1,ty-8,dc);
        g.fill(cx-3,ty-16,cx+4,ty-11,DonutConfig.wa(DonutConfig.GOLD,120));
        Draw.gradientText(g,f,"D O N U T  S M P",cx,ty,DonutConfig.PURPLE,DonutConfig.CYAN);
        Draw.glowText(g,f,"C L I E N T",cx,ty+14,DonutConfig.wa(DonutConfig.CYAN,255));
        Draw.separator(g,cx,ty+28,90,DonutConfig.PURPLE,DonutConfig.CYAN);
        Draw.text(g,f,tag,cx,ty+36,DonutConfig.wa(DonutConfig.MID,220));
        Draw.text(g,f,"Minecraft 1.21.1",cx,ty+50,DonutConfig.wa(DonutConfig.DIM,200));
        int by=height/2+15;Draw.separator(g,cx,by-10,120,DonutConfig.PURPLE,DonutConfig.CYAN);
        Draw.separator(g,cx,by+BG*2+BH+6,120,DonutConfig.CYAN,DonutConfig.PURPLE);
        Draw.sideDecor(g,width,height,time);}

    @Override public boolean shouldCloseOnEsc(){return false;}
}
