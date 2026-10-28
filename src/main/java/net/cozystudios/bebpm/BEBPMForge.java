//? if forge {
/*package net.cozystudios.bebpm;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(BEBPM.MOD_ID)
public final class BEBPMForge {
    public BEBPMForge(FMLJavaModLoadingContext context) {
        BEBPM.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            BEBPMForgeClient.init(context);
        }
    }
}
*///?}
