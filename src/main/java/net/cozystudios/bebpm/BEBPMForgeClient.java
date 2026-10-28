//? if forge {
/*package net.cozystudios.bebpm;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//? if cloth {
import net.minecraftforge.client.ConfigScreenHandler;
//?}

public final class BEBPMForgeClient {

    private BEBPMForgeClient() {
    }

    public static void init(FMLJavaModLoadingContext context) {
        //? if cloth {
        if (BEBPMPlatform.isModLoaded("cloth_config")) {
            context.getContainer().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (minecraft, parent) -> BEBPMConfigScreen.create(parent)));
        }
        //?}
    }
}
*///?}
