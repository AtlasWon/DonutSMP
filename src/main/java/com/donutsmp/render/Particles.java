package com.donutsmp.render;

import com.donutsmp.config.DonutConfig;
import net.minecraft.client.gui.GuiGraphics;
import java.util.ArrayList;import java.util.List;import java.util.Random;

public class Particles {
    private final List<P> ps=new ArrayList<>();private final Random r=new Random();private int w,h;private boolean ok;
    public void init(int w,int h){this.w=w;this.h=h;ps.clear();for(int i=0;i<DonutConfig.PARTICLE_COUNT.get();i++)ps.add(mk(true));ok=true;}
    private P mk(boolean ry){P p=new P();p.x=r.nextFloat()*w;p.y=ry?r.nextFloat()*h:h+10+r.nextFloat()*30;
        p.sz=1.2f+r.nextFloat()*2.5f;p.vx=(r.nextFloat()-0.5f)*0.2f;p.vy=-(0.1f+r.nextFloat()*0.28f);
        p.a=0.12f+r.nextFloat()*0.33f;p.ma=p.a;p.ps=0.004f+r.nextFloat()*0.01f;p.po=r.nextFloat()*6.28f;
        float t=r.nextFloat();p.cr=(int)(DonutConfig.r(DonutConfig.PURPLE)*(1-t)+DonutConfig.r(DonutConfig.CYAN)*t);
        p.cg=(int)(DonutConfig.g(DonutConfig.PURPLE)*(1-t)+DonutConfig.g(DonutConfig.CYAN)*t);
        p.cb=(int)(DonutConfig.b(DonutConfig.PURPLE)*(1-t)+DonutConfig.b(DonutConfig.CYAN)*t);
        if(r.nextFloat()<0.1f){p.cr=DonutConfig.r(DonutConfig.PINK);p.cg=DonutConfig.g(DonutConfig.PINK);p.cb=DonutConfig.b(DonutConfig.PINK);}return p;}
    public void tick(){if(!ok||!DonutConfig.PARTICLES_ENABLED.get())return;for(int i=ps.size()-1;i>=0;i--){P p=ps.get(i);p.age+=1;
        p.x+=p.vx+(float)Math.sin(p.age*0.008)*0.12f;p.y+=p.vy;p.a=p.ma*(0.5f+0.5f*(float)Math.sin(p.age*p.ps+p.po));
        if(p.y<-15||p.x<-15||p.x>w+15)ps.set(i,mk(false));}}
    public void render(GuiGraphics g){if(!ok||!DonutConfig.PARTICLES_ENABLED.get())return;for(P p:ps){int a=Math.max(0,Math.min(255,(int)(p.a*255)));if(a<4)continue;
        int cx=(int)p.x,cy=(int)p.y,hs=Math.max(1,(int)(p.sz/2));
        g.fill(cx-hs*2,cy-hs*2,cx+hs*2,cy+hs*2,DonutConfig.argb(a/5,p.cr,p.cg,p.cb));
        g.fill(cx-hs,cy-hs,cx+hs,cy+hs,DonutConfig.argb(a,p.cr,p.cg,p.cb));
        g.fill(cx,cy,cx+1,cy+1,DonutConfig.argb(Math.min(255,a+40),Math.min(255,p.cr+50),Math.min(255,p.cg+50),Math.min(255,p.cb+50)));}}
    private static class P{float x,y,vx,vy,sz,a,ma,ps,po,age;int cr,cg,cb;}
}
