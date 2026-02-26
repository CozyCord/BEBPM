package net.cozystudios.bebpm.mixin;

//? if >=1.21 {
/*import net.minecraft.registry.tag.BlockTags;
*///?} else {
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
//?}
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {

    //? if <1.21 {
    private static final TagKey<Block> POWER_PROVIDER_TAG = TagKey.of(
            RegistryKeys.BLOCK, new Identifier("minecraft", "enchantment_power_provider")
    );
    //?}

    @Shadow @Final @Mutable
    private static List<BlockPos> POWER_PROVIDER_OFFSETS;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void bebpm$expandBookshelfRange(CallbackInfo ci) {
        List<BlockPos> expanded = new ArrayList<>();
        for (int y = -3; y <= 10; y++) {
            for (int x = -3; x <= 3; x++) {
                for (int z = -3; z <= 3; z++) {
                    if (Math.abs(x) >= 2 || Math.abs(z) >= 2) {
                        expanded.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        POWER_PROVIDER_OFFSETS = expanded;
    }

    @Inject(method = "canAccessPowerProvider", at = @At("HEAD"), cancellable = true)
    private static void bebpm$skipAirGapCheck(World world, BlockPos tablePos, BlockPos providerOffset, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = world.getBlockState(tablePos.add(providerOffset));
        //? if >=1.21 {
        /*cir.setReturnValue(state.isIn(BlockTags.ENCHANTMENT_POWER_PROVIDER));
        *///?} else {
        cir.setReturnValue(state.isIn(POWER_PROVIDER_TAG));
        //?}
    }
}
