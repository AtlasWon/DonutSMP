package com.donutsmp.screen;

import com.donutsmp.config.DonutConfig;
import com.donutsmp.render.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;

public class OMultiScreen extends JoinMultiplayerScreen {
    private final Particles p=new Particles();private long t0=System.currentTimeMillis();private boolean pr;
    public OMultiScreen(Screen parent){super(parent);}
    @Override public void renderBackground(GuiGraphics g,int mx,int my,float pt){
        float t=(float)(System.currentTimeMillis()-t0);if(!pr){p.init(width,height);pr=true;}
        Draw.background(g,width,height,t);p.tick();p.render(g);
        Draw.headerBar(g,font,width,height,"M U L T I P L A Y E R",DonutConfig.CYAN,t);Draw.footerBar(g,width,height);}
}
