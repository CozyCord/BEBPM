package net.cozystudios.bebpm.mixin;

import net.cozystudios.bebpm.BEBPMConfig;
import net.cozystudios.bebpm.BEBPMLineOfSight;
import net.cozystudios.bebpm.BEBPMOffsets;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.registry.tag.BlockTags;
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

import java.util.List;

@Mixin(value = EnchantingTableBlock.class, priority = 100)
public class EnchantingTableBlockMixin {

    @Shadow @Final @Mutable
    private static List<BlockPos> POWER_PROVIDER_OFFSETS;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void bebpm$expandBookshelfRange(CallbackInfo ci) {
        BEBPMOffsets.setApplier(offsets -> POWER_PROVIDER_OFFSETS = offsets);
        BEBPMOffsets.refresh();
    }

    @Inject(method = "canAccessPowerProvider", at = @At("HEAD"), cancellable = true)
    private static void bebpm$decidePower(World world, BlockPos tablePos, BlockPos providerOffset,
                                          CallbackInfoReturnable<Boolean> cir) {
        BlockState state = world.getBlockState(tablePos.add(providerOffset));
        if (!state.isIn(BlockTags.ENCHANTMENT_POWER_PROVIDER)) {
            return;
        }

        if (!BEBPMLineOfSight.allows(world, tablePos, providerOffset, BEBPMConfig.get().lineOfSight)) {
            return;
        }

        cir.setReturnValue(true);
    }
}
