package net.cozystudios.bebpm;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
//? if <1.21.11 {
import net.minecraft.resources.ResourceLocation;
//?} else {
/*import net.minecraft.resources.Identifier;
*///?}
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class BEBPMIds {

    private BEBPMIds() {
    }

    public static String normalise(String raw) {
        //? if <1.21.11 {
        ResourceLocation id = ResourceLocation.tryParse(raw);
        //?} else {
        /*Identifier id = Identifier.tryParse(raw);
        *///?}
        return id == null ? null : id.toString();
    }

    public static TagKey<Block> blockTag(String id) {
        //? if <1.21 {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(id));
        //?} elif <1.21.11 {
        /*return TagKey.create(Registries.BLOCK, ResourceLocation.parse(id));
        *///?} else {
        /*return TagKey.create(Registries.BLOCK, Identifier.parse(id));
        *///?}
    }

    public static TagKey<Block> blockTag(String namespace, String path) {
        //? if <1.21 {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(namespace, path));
        //?} elif <1.21.11 {
        /*return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, path));
        *///?} else {
        /*return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(namespace, path));
        *///?}
    }

    public static String blockId(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).toString();
    }
}
