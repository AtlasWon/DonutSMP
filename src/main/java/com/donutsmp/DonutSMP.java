package com.donutsmp;

import com.donutsmp.config.DonutConfig;
import com.donutsmp.event.ClientEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.BusGroup;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@Mod(DonutSMP.MOD_ID)
public class DonutSMP {
    public static final String MOD_ID = "donutsmp";
    public static final String MOD_NAME = "DonutSMP";
    public static final String MOD_VERSION = "1.1.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public DonutSMP(FMLJavaModLoadingContext context) {
        LOGGER.info("[DonutSMP] Initializing...");
        context.registerConfig(ModConfig.Type.CLIENT, DonutConfig.SPEC, "donutsmp-client.toml");
        if (FMLEnvironment.dist == Dist.CLIENT) {
            BusGroup.DEFAULT.register(MethodHandles.lookup(), ClientEvents.class);
            LOGGER.info("[DonutSMP] Client events registered.");
        }
        LOGGER.info("[DonutSMP] v{} loaded.", MOD_VERSION);
    }
}
