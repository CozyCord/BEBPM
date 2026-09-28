package net.cozystudios.bebpm;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BEBPMLineOfSight {

    private static final TagKey<Block> POWER_PERMEABLE = BEBPMIds.blockTag(BEBPM.MOD_ID, "power_permeable");

    private static List<TagKey<Block>> userTags = List.of();
    private static Set<String> userBlocks = Set.of();

    private BEBPMLineOfSight() {
    }

    public static void setUserPermeable(List<String> entries) {
        List<TagKey<Block>> tags = new ArrayList<>();
        Set<String> blocks = new HashSet<>();

        if (entries != null) {
            for (String raw : entries) {
                String entry = raw == null ? "" : raw.trim();
                if (entry.isEmpty()) {
                    continue;
                }
                boolean isTag = entry.startsWith("#");
                String id = BEBPMIds.normalise(isTag ? entry.substring(1) : entry);
                if (id == null) {
                    BEBPM.LOGGER.warn("Ignoring unparseable permeable entry '{}'", raw);
                    continue;
                }
                if (isTag) {
                    tags.add(BEBPMIds.blockTag(id));
                } else {
                    blocks.add(id);
                }
            }
        }

        userTags = List.copyOf(tags);
        userBlocks = Set.copyOf(blocks);
    }

    public static boolean isValidEntry(String raw) {
        String entry = raw == null ? "" : raw.trim();
        if (entry.isEmpty()) {
            return false;
        }
        return BEBPMIds.normalise(entry.startsWith("#") ? entry.substring(1) : entry) != null;
    }

    public static boolean allows(Level level, BlockPos tablePos, BlockPos providerOffset,
                                 BEBPMConfig.LineOfSight mode) {
        if (mode == BEBPMConfig.LineOfSight.OFF) {
            return true;
        }

        int dx = providerOffset.getX();
        int dy = providerOffset.getY();
        int dz = providerOffset.getZ();

        int steps = Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz)));
        if (steps <= 1) {
            return true;
        }

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int step = 1; step < steps; step++) {
            double along = (double) step / steps;
            int x = tablePos.getX() + (int) Math.round(dx * along);
            int y = tablePos.getY() + (int) Math.round(dy * along);
            int z = tablePos.getZ() + (int) Math.round(dz * along);

            if (x == tablePos.getX() && y == tablePos.getY() && z == tablePos.getZ()) {
                continue;
            }

            cursor.set(x, y, z);
            BlockState state = level.getBlockState(cursor);

            if (mode == BEBPMConfig.LineOfSight.ON) {
                if (!state.is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)) {
                    return false;
                }
            } else if (state.isCollisionShapeFullBlock(level, cursor) && !isPermeable(state)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPermeable(BlockState state) {
        if (state.is(POWER_PERMEABLE)) {
            return true;
        }
        for (TagKey<Block> tag : userTags) {
            if (state.is(tag)) {
                return true;
            }
        }
        return !userBlocks.isEmpty() && userBlocks.contains(BEBPMIds.blockId(state.getBlock()));
    }
}
