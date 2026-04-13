package com.donutsmp;

import com.donutsmp.config.DonutConfig;
import com.donutsmp.event.ClientEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(DonutSMP.MOD_ID)
public class DonutSMP {
    public static final String MOD_ID = "donutsmp";
    public static final String MOD_NAME = "DonutSMP";
    public static final String MOD_VERSION = "1.1.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SuppressWarnings("removal")
    public DonutSMP() {
        LOGGER.info("[DonutSMP] Initializing...");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DonutConfig.SPEC, "donutsmp-client.toml");
        if (FMLEnvironment.dist == Dist.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        }
        LOGGER.info("[DonutSMP] v{} loaded.", MOD_VERSION);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        ClientEvents.register();
        LOGGER.info("[DonutSMP] Client setup complete.");
    }
}
