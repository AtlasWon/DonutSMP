package com.donutsmp.render;

import com.donutsmp.config.DonutConfig;
import net.minecraft.client.gui.GuiGraphics;
import java.util.ArrayList;import java.util.List;import java.util.Random;

public class Starfield {
    private static final List<S> stars=new ArrayList<>();private static int lw,lh;
    public static void render(GuiGraphics g,int w,int h,float t){
        if(w!=lw||h!=lh||stars.isEmpty()){lw=w;lh=h;stars.clear();Random r=new Random(42);
            for(int i=0;i<80+(w*h)/9000;i++){S s=new S();s.x=r.nextFloat()*w;s.y=r.nextFloat()*h;
                s.b=0.2f+r.nextFloat()*0.6f;s.sp=0.001f+r.nextFloat()*0.004f;s.o=r.nextFloat()*6.28f;
                s.sz=r.nextFloat()<0.85f?1:2;stars.add(s);}}
        g.fill(0,0,w,h,0xFF0A0A16);g.fillGradient(0,0,w,h,0xFF0A0A16,0xFF0F0C24);
        for(int i=0;i<3;i++){float off=(float)(Math.sin(t*0.00025*(i+1)+i*1.8)*h*0.2);
            int cy=h/2+(int)off;int bh=h/7;int top=Math.max(0,cy-bh),bot=Math.min(h,cy+bh);if(top>=bot)continue;
            int a=4+i*2;int c=i==0?DonutConfig.PURPLE:i==1?DonutConfig.CYAN:DonutConfig.PINK;
            g.fillGradient(0,top,w,cy,0,DonutConfig.wa(c,a));g.fillGradient(0,cy,w,bot,DonutConfig.wa(c,a),0);}
        for(S s:stars){float tw=(float)(Math.sin(t*s.sp+s.o)*0.5+0.5);int a=(int)(s.b*tw*220);if(a<6)continue;
            int x=(int)s.x,y=(int)s.y;if(s.sz==1)g.fill(x,y,x+1,y+1,DonutConfig.argb(a,210,210,245));
            else{g.fill(x,y,x+2,y+2,DonutConfig.argb(a,230,230,255));g.fill(x-1,y,x+3,y+1,DonutConfig.argb(a/3,190,190,230));
                g.fill(x,y-1,x+1,y+3,DonutConfig.argb(a/3,190,190,230));}}
        int gs=Math.min(w,h)/3;float p1=(float)(Math.sin(t*0.0007)*0.5+0.5);
        g.fillGradient(0,0,gs,gs,DonutConfig.wa(DonutConfig.PURPLE,(int)(p1*14)),0);
        float p2=(float)(Math.sin(t*0.0005+2)*0.5+0.5);
        g.fillGradient(w-gs,h-gs,w,h,0,DonutConfig.wa(DonutConfig.CYAN,(int)(p2*12)));}
    private static class S{float x,y,b,sp,o;int sz;}
}
