//? if cloth {
package net.cozystudios.bebpm;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.widget.SearchFieldEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public final class BEBPMConfigScreen {

    private BEBPMConfigScreen() {
    }

    public static Screen create(Screen parent) {
        BEBPMConfig config = BEBPMConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("bebpm.config.title"))
                .setSavingRunnable(config::save)
                .setAfterInitConsumer(BEBPMConfigScreen::removeSearchBar);

        ConfigCategory category = builder.getOrCreateCategory(
                Component.translatable("bebpm.config.category.range"));
        category.addEntry(slider("bebpm.config.radius_x", config.radiusX,
                BEBPMConfig.VANILLA_RADIUS_X, value -> config.radiusX = value, 1));

        category.addEntry(slider("bebpm.config.radius_z", config.radiusZ,
                BEBPMConfig.VANILLA_RADIUS_Z, value -> config.radiusZ = value, 1));

        category.addEntry(slider("bebpm.config.radius_up", config.radiusUp,
                BEBPMConfig.VANILLA_RADIUS_UP, value -> config.radiusUp = value, 1));

        category.addEntry(slider("bebpm.config.radius_down", config.radiusDown,
                BEBPMConfig.VANILLA_RADIUS_DOWN, value -> config.radiusDown = value, 1));

        category.addEntry(new BEBPMEnumEntry<>(
                Component.translatable("bebpm.config.line_of_sight"),
                BEBPMConfig.LineOfSight.class, config.lineOfSight,
                () -> BEBPMConfig.DEFAULT_LINE_OF_SIGHT,
                value -> config.lineOfSight = value,
                BEBPMConfigScreen::lineOfSightName,
                () -> Optional.of(lineOfSightTooltip())));

        category.addEntry(new BEBPMStringListEntry(
                Component.translatable("bebpm.config.decor_permeable"),
                new ArrayList<>(config.decorPermeable),
                ArrayList::new,
                value -> config.decorPermeable = new ArrayList<>(value),
                () -> Optional.of(tooltip("bebpm.config.decor_permeable", 3))));

        return builder.build();
    }

    private static ChatFormatting lineOfSightColour(BEBPMConfig.LineOfSight state) {
        return switch (state) {
            case OFF -> ChatFormatting.GREEN;
            case DECOR_ONLY -> ChatFormatting.GOLD;
            case ON -> ChatFormatting.RED;
        };
    }

    private static Component lineOfSightName(Enum<?> value) {
        Component name = Component.translatable("bebpm.config.line_of_sight."
                + value.name().toLowerCase(Locale.ROOT));
        return value instanceof BEBPMConfig.LineOfSight state
                ? name.copy().withStyle(lineOfSightColour(state))
                : name;
    }

    private static Component[] lineOfSightTooltip() {
        return new Component[]{
                labelled(BEBPMConfig.LineOfSight.OFF, "off.1"),
                Component.translatable("bebpm.config.line_of_sight.off.2"),
                labelled(BEBPMConfig.LineOfSight.DECOR_ONLY, "decor_only.1"),
                Component.translatable("bebpm.config.line_of_sight.decor_only.2"),
                labelled(BEBPMConfig.LineOfSight.ON, "on.1"),
        };
    }

    private static Component labelled(BEBPMConfig.LineOfSight state, String bodyKey) {
        String key = state.name().toLowerCase(Locale.ROOT);
        return Component.empty()
                .append(Component.translatable("bebpm.config.line_of_sight." + key + ".label")
                        .withStyle(lineOfSightColour(state)))
                .append(" ")
                .append(Component.translatable("bebpm.config.line_of_sight." + bodyKey));
    }

    private static void removeSearchBar(Screen screen) {
        if (screen instanceof ClothConfigScreen configScreen) {
            configScreen.listWidget.children().removeIf(SearchFieldEntry.class::isInstance);
        }
    }

    private static BEBPMSliderEntry slider(String key, int value, int vanillaFloor,
                                           Consumer<Integer> save, int tooltipLines) {
        Component[] tooltip = tooltip(key, tooltipLines);
        BEBPMSliderEntry entry = new BEBPMSliderEntry(Component.translatable(key),
                BEBPMConfig.SLIDER_MIN, BEBPMConfig.MAX_RADIUS, value,
                () -> BEBPMConfig.DEFAULT_RADIUS, save, () -> Optional.of(tooltip));
        snapToTrack(entry, vanillaFloor);
        return entry;
    }

    private static Component[] tooltip(String key, int lines) {
        Component[] texts = new Component[lines];
        for (int i = 0; i < lines; i++) {
            texts[i] = Component.translatable(key + ".tooltip." + (i + 1));
        }
        return texts;
    }

    private static void snapToTrack(BEBPMSliderEntry entry, int vanillaFloor) {
        boolean[] snapping = {false};
        entry.setTextGetter(value -> {
            if (!snapping[0]) {
                snapping[0] = true;
                try {
                    entry.setValue(value);
                } finally {
                    snapping[0] = false;
                }
            }
            return value <= vanillaFloor
                    ? Component.translatable("bebpm.config.vanilla")
                    : Component.literal(String.valueOf(value));
        });
    }
}
//?}
