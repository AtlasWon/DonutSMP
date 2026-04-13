package com.donutsmp.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DonutConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.BooleanValue PARTICLES_ENABLED;
    public static final ForgeConfigSpec.IntValue PARTICLE_COUNT;
    public static final ForgeConfigSpec.ConfigValue<String> CUSTOM_BG_PATH;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("general"); ENABLED = b.define("enabled", true); b.pop();
        b.push("visuals");
        PARTICLES_ENABLED = b.define("particlesEnabled", true);
        PARTICLE_COUNT = b.defineInRange("particleCount", 50, 0, 200);
        b.pop();
        b.push("background");
        CUSTOM_BG_PATH = b.define("customBgPath", "");
        b.pop();
        SPEC = b.build();
    }

    public static final int PURPLE=0xFF7C4DFF, CYAN=0xFF00E5FF, PINK=0xFFFF4081, GOLD=0xFFFFD740;
    public static final int WHITE=0xFFFFFFFF, BRIGHT=0xFFE8E8F4, MID=0xFFB0B0C8;
    public static final int DIM=0xFF7878A0, FAINT=0xFF4A4A68, BORDER=0xFF2A2A48;

    public static int argb(int a,int r,int g,int b){return((a&0xFF)<<24)|((r&0xFF)<<16)|((g&0xFF)<<8)|(b&0xFF);}
    public static int r(int c){return(c>>16)&0xFF;}
    public static int g(int c){return(c>>8)&0xFF;}
    public static int b(int c){return c&0xFF;}
    public static int wa(int c,int a){return(c&0x00FFFFFF)|((a&0xFF)<<24);}
    public static int mix(int c1,int c2,float t){
        return argb(255,(int)(r(c1)*(1-t)+r(c2)*t),(int)(g(c1)*(1-t)+g(c2)*t),(int)(b(c1)*(1-t)+b(c2)*t));
    }
}
