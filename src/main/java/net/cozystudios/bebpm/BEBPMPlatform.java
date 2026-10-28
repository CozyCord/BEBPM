package net.cozystudios.bebpm;

import java.nio.file.Path;

public final class BEBPMPlatform {

    private BEBPMPlatform() {
    }

    public static Path configDir() {
        //? if fabric {
        return net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir();
        //?} elif neoforge {
        /*return net.neoforged.fml.loading.FMLPaths.CONFIGDIR.get();
        *///?} else {
        /*return net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR.get();
        *///?}
    }

    public static boolean isModLoaded(String id) {
        //? if fabric {
        return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded(id);
        //?} elif neoforge {
        /*return net.neoforged.fml.ModList.get().isLoaded(id);
        *///?} elif >=26.1 {
        /*return net.minecraftforge.fml.ModList.isLoaded(id);
        *///?} else {
        /*return net.minecraftforge.fml.ModList.get().isLoaded(id);
        *///?}
    }
}
