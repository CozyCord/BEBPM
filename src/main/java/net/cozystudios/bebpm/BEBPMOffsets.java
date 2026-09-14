package net.cozystudios.bebpm;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class BEBPMOffsets {

    private static Consumer<List<BlockPos>> applier;

    private BEBPMOffsets() {
    }

    public static void setApplier(Consumer<List<BlockPos>> consumer) {
        applier = consumer;
    }

    public static void refresh() {
        if (applier != null) {
            applier.accept(build());
        }
    }

    private static List<BlockPos> build() {
        BEBPMConfig config = BEBPMConfig.get();
        int rx = config.effectiveRadiusX();
        int rz = config.effectiveRadiusZ();
        int up = config.effectiveRadiusUp();
        int down = config.effectiveRadiusDown();

        List<BlockPos> offsets = new ArrayList<>((2 * rx + 1) * (up + down + 1) * (2 * rz + 1));
        for (int y = -down; y <= up; y++) {
            for (int x = -rx; x <= rx; x++) {
                for (int z = -rz; z <= rz; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    offsets.add(new BlockPos(x, y, z));
                }
            }
        }
        return offsets;
    }
}
