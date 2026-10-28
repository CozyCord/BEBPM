package net.cozystudios.bebpm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BEBPM {
    public static final String MOD_ID = "bebpm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private BEBPM() {
    }

    public static void init() {
        LOGGER.info("BEBPM loaded - bookshelf enchanting power rules broken!");
    }
}
