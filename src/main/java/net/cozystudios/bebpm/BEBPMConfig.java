package net.cozystudios.bebpm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BEBPMConfig {

    public static final int SLIDER_MIN = 0;
    public static final int MAX_RADIUS = 16;

    public static final int VANILLA_RADIUS_X = 2;
    public static final int VANILLA_RADIUS_Z = 2;
    public static final int VANILLA_RADIUS_UP = 1;
    public static final int VANILLA_RADIUS_DOWN = 0;

    public static final int DEFAULT_RADIUS = 10;
    public static final LineOfSight DEFAULT_LINE_OF_SIGHT = LineOfSight.OFF;

    public enum LineOfSight {
        OFF,
        DECOR_ONLY,
        ON
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static BEBPMConfig instance;

    public int radiusX = DEFAULT_RADIUS;
    public int radiusZ = DEFAULT_RADIUS;
    public int radiusUp = DEFAULT_RADIUS;
    public int radiusDown = DEFAULT_RADIUS;
    public LineOfSight lineOfSight = DEFAULT_LINE_OF_SIGHT;

    public List<String> decorPermeable = new ArrayList<>();

    private Boolean requireLineOfSight;

    public int effectiveRadiusX() {
        return Math.max(radiusX, VANILLA_RADIUS_X);
    }

    public int effectiveRadiusZ() {
        return Math.max(radiusZ, VANILLA_RADIUS_Z);
    }

    public int effectiveRadiusUp() {
        return Math.max(radiusUp, VANILLA_RADIUS_UP);
    }

    public int effectiveRadiusDown() {
        return Math.max(radiusDown, VANILLA_RADIUS_DOWN);
    }

    public static BEBPMConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve(BEBPM.MOD_ID + ".json");
    }

    private static BEBPMConfig load() {
        Path path = path();
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                BEBPMConfig loaded = GSON.fromJson(reader, BEBPMConfig.class);
                if (loaded != null) {
                    loaded.migrate();
                    loaded.clamp();
                    loaded.applyPermeable();
                    return loaded;
                }
            } catch (Exception exception) {
                BEBPM.LOGGER.warn("Could not read {}, falling back to defaults.", path, exception);
            }
        }
        BEBPMConfig fresh = new BEBPMConfig();
        fresh.write();
        fresh.applyPermeable();
        return fresh;
    }

    public void save() {
        write();
        applyPermeable();
        BEBPMOffsets.refresh();
    }

    private void applyPermeable() {
        BEBPMLineOfSight.setUserPermeable(decorPermeable);
    }

    private void write() {
        clamp();
        Path path = path();
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (Exception exception) {
            BEBPM.LOGGER.warn("Could not write {}.", path, exception);
        }
    }

    private void migrate() {
        if (requireLineOfSight != null) {
            lineOfSight = requireLineOfSight ? LineOfSight.ON : LineOfSight.OFF;
            requireLineOfSight = null;
        }
        if (lineOfSight == null) {
            lineOfSight = DEFAULT_LINE_OF_SIGHT;
        }
        if (decorPermeable == null) {
            decorPermeable = new ArrayList<>();
        }
    }

    private void clamp() {
        radiusX = clamp(radiusX);
        radiusZ = clamp(radiusZ);
        radiusUp = clamp(radiusUp);
        radiusDown = clamp(radiusDown);
    }

    private static int clamp(int value) {
        return Math.max(SLIDER_MIN, Math.min(MAX_RADIUS, value));
    }
}
