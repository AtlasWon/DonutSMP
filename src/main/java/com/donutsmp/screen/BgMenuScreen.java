package com.donutsmp.screen;

import com.donutsmp.config.DonutConfig;
import com.donutsmp.render.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BgMenuScreen extends Screen {
    private final Screen parent;private final Particles particles=new Particles();private long t0=System.currentTimeMillis();
    public BgMenuScreen(Screen parent){super(Component.literal("Background"));this.parent=parent;}
    @Override protected void init(){super.init();particles.init(width,height);int cx=width/2,cy=height/2,bw=220,bh=24,gap=32;
        addRenderableWidget(new OButton(cx-bw/2,cy-gap-bh/2,bw,bh,Component.literal("\u25A3  IMAGE"),b->{CustomBg.pickImage();minecraft.setScreen(parent);},DonutConfig.PURPLE));
        addRenderableWidget(new OButton(cx-bw/2,cy-bh/2,bw,bh,Component.literal("\u25B6  VIDEO (GIF)"),b->{CustomBg.pickVideo();minecraft.setScreen(parent);},DonutConfig.CYAN));
        addRenderableWidget(new OButton(cx-bw/2,cy+gap-bh/2,bw,bh,Component.literal("\u21BA  RESET"),b->{CustomBg.reset();minecraft.setScreen(parent);},DonutConfig.PINK));
        addRenderableWidget(new OButton(cx-bw/2,cy+gap*2+8-bh/2,bw,bh,Component.literal("\u2190  BACK"),b->minecraft.setScreen(parent),DonutConfig.GOLD));}
    @Override public void renderBackground(GuiGraphics g,int mx,int my,float pt){float t=(float)(System.currentTimeMillis()-t0);
        Draw.background(g,width,height,t);particles.tick();particles.render(g);}
    @Override public void render(GuiGraphics g,int mx,int my,float pt){super.render(g,mx,my,pt);float t=(float)(System.currentTimeMillis()-t0);
        Draw.card(g,width/2-130,height/2-100,260,44,DonutConfig.GOLD,t);
        Draw.glowText(g,font,"B A C K G R O U N D",width/2,height/2-86,DonutConfig.wa(DonutConfig.GOLD,255));
        Draw.text(g,font,"Customize your client",width/2,height/2-72,DonutConfig.wa(DonutConfig.MID,200));
        String s=CustomBg.isActive()?"Custom background active":"Default starfield";
        Draw.text(g,font,s,width/2,height-30,DonutConfig.wa(DonutConfig.DIM,180));}
    @Override public boolean shouldCloseOnEsc(){return true;}
    @Override public void onClose(){minecraft.setScreen(parent);}
}
