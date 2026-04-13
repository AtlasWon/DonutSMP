package com.donutsmp.screen;

import com.donutsmp.config.DonutConfig;
import com.donutsmp.render.*;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;

public class OOptionsScreen extends OptionsScreen {
    private final Particles p=new Particles();private long t0=System.currentTimeMillis();private boolean pr;
    public OOptionsScreen(Screen parent,Options opts){super(parent,opts);}
    @Override public void renderBackground(GuiGraphics g,int mx,int my,float pt){
        float t=(float)(System.currentTimeMillis()-t0);if(!pr){p.init(width,height);pr=true;}
        Draw.background(g,width,height,t);p.tick();p.render(g);
        Draw.headerBar(g,font,width,height,"O P T I O N S",DonutConfig.PINK,t);Draw.footerBar(g,width,height);}
}
