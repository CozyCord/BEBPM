//? if neoforge {
/*package net.cozystudios.bebpm;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
//? if cloth {
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//?}

@Mod(value = BEBPM.MOD_ID, dist = Dist.CLIENT)
public final class BEBPMNeoForgeClient {
    public BEBPMNeoForgeClient(ModContainer container) {
        //? if cloth {
        if (BEBPMPlatform.isModLoaded("cloth_config")) {
            container.registerExtensionPoint(IConfigScreenFactory.class,
                    (modContainer, parent) -> BEBPMConfigScreen.create(parent));
        }
        //?}
    }
}
*///?}
