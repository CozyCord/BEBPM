package net.cozystudios.bebpm;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BEBPMLineOfSight {

    private static final TagKey<Block> POWER_PERMEABLE = TagKey.of(
            RegistryKeys.BLOCK, Identifier.of(BEBPM.MOD_ID, "power_permeable")
    );

    private static List<TagKey<Block>> userTags = List.of();
    private static Set<Identifier> userBlocks = Set.of();

    private BEBPMLineOfSight() {
    }

    public static void setUserPermeable(List<String> entries) {
        List<TagKey<Block>> tags = new ArrayList<>();
        Set<Identifier> blocks = new HashSet<>();

        if (entries != null) {
            for (String raw : entries) {
                String entry = raw == null ? "" : raw.trim();
                if (entry.isEmpty()) {
                    continue;
                }
                boolean isTag = entry.startsWith("#");
                Identifier id = Identifier.tryParse(isTag ? entry.substring(1) : entry);
                if (id == null) {
                    BEBPM.LOGGER.warn("Ignoring unparseable permeable entry '{}'", raw);
                    continue;
                }
                if (isTag) {
                    tags.add(TagKey.of(RegistryKeys.BLOCK, id));
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
        return Identifier.tryParse(entry.startsWith("#") ? entry.substring(1) : entry) != null;
    }

    public static boolean allows(World world, BlockPos tablePos, BlockPos providerOffset,
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

        BlockPos.Mutable cursor = new BlockPos.Mutable();
        for (int step = 1; step < steps; step++) {
            double along = (double) step / steps;
            int x = tablePos.getX() + (int) Math.round(dx * along);
            int y = tablePos.getY() + (int) Math.round(dy * along);
            int z = tablePos.getZ() + (int) Math.round(dz * along);

            if (x == tablePos.getX() && y == tablePos.getY() && z == tablePos.getZ()) {
                continue;
            }

            cursor.set(x, y, z);
            BlockState state = world.getBlockState(cursor);

            if (mode == BEBPMConfig.LineOfSight.ON) {
                if (!state.isIn(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)) {
                    return false;
                }
            } else if (state.isFullCube(world, cursor) && !isPermeable(state)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPermeable(BlockState state) {
        if (state.isIn(POWER_PERMEABLE)) {
            return true;
        }
        for (TagKey<Block> tag : userTags) {
            if (state.isIn(tag)) {
                return true;
            }
        }
        return !userBlocks.isEmpty() && userBlocks.contains(Registries.BLOCK.getId(state.getBlock()));
    }
}
