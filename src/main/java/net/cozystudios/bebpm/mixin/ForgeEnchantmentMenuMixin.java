package net.cozystudios.bebpm.mixin;

import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EnchantmentMenu.class)
public class ForgeEnchantmentMenuMixin {

    @ModifyConstant(
            method = "*(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            constant = @Constant(floatValue = 1.0F, ordinal = 0), remap = false)
    private float bebpm$dropVanillaIncrement(float one) {
        return 0.0F;
    }
}
