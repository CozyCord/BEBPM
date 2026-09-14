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

@Mixin(value = EnchantingTableBlock.class, priority = 100)
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
        final int reach = 10;
        final int span = 2 * reach + 1;

        List<BlockPos> expanded = new ArrayList<>(span * span * span);
        for (int y = -reach; y <= reach; y++) {
            for (int x = -reach; x <= reach; x++) {
                for (int z = -reach; z <= reach; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    expanded.add(new BlockPos(x, y, z));
                }
            }
        }
        POWER_PROVIDER_OFFSETS = expanded;
    }

    @Inject(method = "canAccessPowerProvider", at = @At("HEAD"), cancellable = true)
    private static void bebpm$skipAirGapCheck(World world, BlockPos tablePos, BlockPos providerOffset, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = world.getBlockState(tablePos.add(providerOffset));
        //? if >=1.21 {
        /*if (state.isIn(BlockTags.ENCHANTMENT_POWER_PROVIDER)) {
            cir.setReturnValue(true);
        }
        *///?} else {
        if (state.isIn(POWER_PROVIDER_TAG)) {
            cir.setReturnValue(true);
        }
        //?}
    }
}
