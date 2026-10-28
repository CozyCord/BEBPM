package net.cozystudios.bebpm.mixin;

import net.cozystudios.bebpm.BEBPMConfig;
import net.cozystudios.bebpm.BEBPMLineOfSight;
import net.cozystudios.bebpm.BEBPMOffsets;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
//? if <1.21 {
import net.minecraft.world.level.block.EnchantmentTableBlock;
//?} else {
/*import net.minecraft.world.level.block.EnchantingTableBlock;
*///?}
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

//? if <1.21 {
@Mixin(value = EnchantmentTableBlock.class, priority = 100)
//?} else {
/*@Mixin(value = EnchantingTableBlock.class, priority = 100)
*///?}
public class EnchantingTableBlockMixin {

    @Shadow @Final @Mutable
    private static List<BlockPos> BOOKSHELF_OFFSETS;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void bebpm$expandBookshelfRange(CallbackInfo ci) {
        BEBPMOffsets.setApplier(offsets -> BOOKSHELF_OFFSETS = offsets);
        BEBPMOffsets.refresh();
    }

    @Inject(method = "isValidBookShelf", at = @At("HEAD"), cancellable = true)
    private static void bebpm$decidePower(Level level, BlockPos tablePos, BlockPos providerOffset,
                                          CallbackInfoReturnable<Boolean> cir) {
        BlockState state = level.getBlockState(tablePos.offset(providerOffset));
        if (!state.is(BlockTags.ENCHANTMENT_POWER_PROVIDER)) {
            return;
        }

        if (BEBPMLineOfSight.allows(level, tablePos, providerOffset, BEBPMConfig.get().lineOfSight)) {
            cir.setReturnValue(true);
        } else if (!BEBPMOffsets.isVanillaOffset(providerOffset)) {
            cir.setReturnValue(false);
        }
    }
}
