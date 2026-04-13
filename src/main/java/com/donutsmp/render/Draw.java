package com.donutsmp.render;

import com.donutsmp.config.DonutConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class Draw {
    public static void text(GuiGraphics g,Font f,String s,int cx,int y,int c){g.drawString(f,s,cx-f.width(s)/2,y,c,false);}
    public static void glowText(GuiGraphics g,Font f,String s,int cx,int y,int c){
        int x=cx-f.width(s)/2;int gl=DonutConfig.wa(c,30);
        g.drawString(f,s,x-1,y,gl,false);g.drawString(f,s,x+1,y,gl,false);
        g.drawString(f,s,x,y-1,gl,false);g.drawString(f,s,x,y+1,gl,false);g.drawString(f,s,x,y,c,false);}
    public static void gradientText(GuiGraphics g,Font f,String s,int cx,int y,int c1,int c2){
        int tw=f.width(s);float x=cx-tw/2f;for(int i=0;i<s.length();i++){float t=s.length()>1?(float)i/(s.length()-1):0;
            g.drawString(f,String.valueOf(s.charAt(i)),(int)x,y,DonutConfig.mix(c1,c2,t),false);x+=f.width(String.valueOf(s.charAt(i)));}}
    public static void separator(GuiGraphics g,int cx,int y,int hw,int c1,int c2){
        for(int i=0;i<hw;i++){float t=(float)i/hw;int a=(int)(t*90);int c=DonutConfig.wa(DonutConfig.mix(c1,c2,t),a);
            g.fill(cx-hw+i,y,cx-hw+i+1,y+1,c);g.fill(cx+hw-i-1,y,cx+hw-i,y+1,c);}}
    public static void card(GuiGraphics g,int x,int y,int w,int h,int ac,float t){
        g.fill(x,y,x+w,y+h,DonutConfig.argb(180,10,10,22));int bc=DonutConfig.wa(DonutConfig.BORDER,60);
        g.fill(x,y,x+w,y+1,bc);g.fill(x,y+h-1,x+w,y+h,bc);g.fill(x,y,x+1,y+h,bc);g.fill(x+w-1,y,x+w,y+h,bc);
        float p=(float)(Math.sin(t*0.001)*0.3+0.7);g.fill(x+8,y,x+w-8,y+1,DonutConfig.wa(ac,(int)(p*120)));
        int cc=DonutConfig.wa(ac,60);g.fill(x+2,y+2,x+6,y+3,cc);g.fill(x+2,y+2,x+3,y+6,cc);
        g.fill(x+w-6,y+2,x+w-2,y+3,cc);g.fill(x+w-3,y+2,x+w-2,y+6,cc);
        g.fill(x+2,y+h-3,x+6,y+h-2,cc);g.fill(x+2,y+h-6,x+3,y+h-2,cc);
        g.fill(x+w-6,y+h-3,x+w-2,y+h-2,cc);g.fill(x+w-3,y+h-6,x+w-2,y+h-2,cc);}
    public static void headerBar(GuiGraphics g,Font f,int w,int h,String title,int ac,float t){
        g.fill(0,0,w,34,DonutConfig.argb(200,8,8,16));g.fill(0,34,w,35,DonutConfig.wa(DonutConfig.BORDER,50));
        glowText(g,f,title,w/2,13,DonutConfig.wa(ac,255));float p=(float)(Math.sin(t*0.001)*0.3+0.7);
        int lw=f.width(title)+40;g.fill(w/2-lw/2,34,w/2+lw/2,35,DonutConfig.wa(ac,(int)(p*90)));}
    public static void footerBar(GuiGraphics g,int w,int h){
        g.fill(0,h-34,w,h,DonutConfig.argb(200,8,8,16));g.fill(0,h-34,w,h-33,DonutConfig.wa(DonutConfig.BORDER,50));}
    public static void spinner(GuiGraphics g,int cx,int cy,float t,int c){
        int n=10;float r=8;float rot=t*0.005f;for(int i=0;i<n;i++){float angle=rot+(float)(i*Math.PI*2/n);
            int dx=(int)(Math.cos(angle)*r),dy=(int)(Math.sin(angle)*r);int a=(int)((float)i/n*180+30);
            g.fill(cx+dx-1,cy+dy-1,cx+dx+1,cy+dy+1,DonutConfig.wa(c,a));}}
    public static void sideDecor(GuiGraphics g,int w,int h,float t){
        int ac=DonutConfig.PURPLE;int my=h/2;float a=(float)(Math.sin(t*0.0008)*0.3+0.5);int la=(int)(a*50);
        g.fill(18,my-65,19,my+65,DonutConfig.wa(ac,la));g.fill(w-19,my-65,w-18,my+65,DonutConfig.wa(ac,la));
        for(int i=-3;i<=3;i++){int dy=my+i*20;int da=(int)(a*90);
            g.fill(16,dy-1,21,dy+2,DonutConfig.wa(ac,da));g.fill(w-21,dy-1,w-16,dy+2,DonutConfig.wa(ac,da));}}
    public static void background(GuiGraphics g,int w,int h,float time){
        if(CustomBg.isActive())CustomBg.render(g,w,h);else Starfield.render(g,w,h,time);}
}
