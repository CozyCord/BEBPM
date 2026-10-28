//? if fabric && cloth {
package net.cozystudios.bebpm;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class BEBPMModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!BEBPMPlatform.isModLoaded("cloth-config")) {
            return parent -> null;
        }
        return BEBPMConfigScreen::create;
    }
}
//?}
