package com.donutsmp;

import com.donutsmp.config.DonutConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
        LOGGER.info("[DonutSMP] v{} loaded.", MOD_VERSION);
    }
}
