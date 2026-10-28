//? if fabric {
package net.cozystudios.bebpm;

import net.fabricmc.api.ModInitializer;

public class BEBPMFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BEBPM.init();
    }
}
//?}
