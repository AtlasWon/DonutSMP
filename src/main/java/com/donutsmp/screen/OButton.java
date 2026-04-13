package com.donutsmp.screen;

import com.donutsmp.config.DonutConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class OButton extends Button {
    private float hv=0f;private final int accent;
    public OButton(int x,int y,int w,int h,Component t,OnPress p,int ac){super(x,y,w,h,t,p,DEFAULT_NARRATION);accent=ac;}
    public OButton(int x,int y,int w,int h,Component t,OnPress p){this(x,y,w,h,t,p,DonutConfig.PURPLE);}
    @Override protected void renderWidget(GuiGraphics g,int mx,int my,float pt){
        hv+=((isHoveredOrFocused()?1f:0f)-hv)*0.14f;if(hv<0.005f)hv=0;
        int x=getX(),y=getY(),w=getWidth(),h=getHeight();Font f=Minecraft.getInstance().font;
        g.fill(x,y,x+w,y+h,DonutConfig.argb(230,14+(int)(hv*14),14+(int)(hv*12),20+(int)(hv*14)));
        if(hv>0.02f)g.fillGradient(x+1,y+1,x+w-1,y+h-1,DonutConfig.wa(accent,(int)(hv*20)),0);
        int ba=(int)(50+hv*200);int bc=hv>0.3f?accent:DonutConfig.BORDER;int bC=DonutConfig.wa(bc,ba);
        g.fill(x,y,x+w,y+1,bC);g.fill(x,y+h-1,x+w,y+h,bC);g.fill(x,y+1,x+1,y+h-1,bC);g.fill(x+w-1,y+1,x+w,y+h-1,bC);
        if(hv>0.05f){int lw=(int)(hv*(w-12));int lx=x+(w-lw)/2;
            g.fill(lx,y+h-2,lx+lw,y+h-1,DonutConfig.wa(accent,(int)(hv*255)));}
        int ca=(int)(35+hv*80);int cc=DonutConfig.wa(accent,ca);
        g.fill(x+2,y+2,x+5,y+3,cc);g.fill(x+2,y+2,x+3,y+5,cc);g.fill(x+w-5,y+2,x+w-2,y+3,cc);g.fill(x+w-3,y+2,x+w-2,y+5,cc);
        g.fill(x+2,y+h-3,x+5,y+h-2,cc);g.fill(x+2,y+h-5,x+3,y+h-2,cc);g.fill(x+w-5,y+h-3,x+w-2,y+h-2,cc);g.fill(x+w-3,y+h-5,x+w-2,y+h-2,cc);
        String text=getMessage().getString();int tw=f.width(text);int tx=x+(w-tw)/2,ty=y+(h-8)/2;
        if(!active){g.drawString(f,text,tx,ty,DonutConfig.wa(DonutConfig.DIM,140),false);return;}
        if(hv>0.4f){int ga=(int)((hv-0.4f)/0.6f*30);g.drawString(f,text,tx-1,ty,DonutConfig.wa(DonutConfig.WHITE,ga),false);
            g.drawString(f,text,tx+1,ty,DonutConfig.wa(DonutConfig.WHITE,ga),false);}
        g.drawString(f,text,tx,ty,hv>0.3f?DonutConfig.WHITE:DonutConfig.BRIGHT,false);}
}
