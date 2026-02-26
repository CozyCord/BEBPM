package net.cozystudios.bebpm;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BEBPM implements ModInitializer {
    public static final String MOD_ID = "bebpm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("BEBPM loaded - bookshelf enchanting power rules broken!");
    }
}
