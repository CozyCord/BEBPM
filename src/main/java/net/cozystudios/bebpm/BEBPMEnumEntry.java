//? if cloth {
package net.cozystudios.bebpm;

import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
//? if >=26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
*///?} else {
import net.minecraft.client.gui.GuiGraphics;
//?}
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BEBPMEnumEntry<T extends Enum<?>> extends EnumListEntry<T> {

    private final Supplier<Optional<Component[]>> tooltipLines;

    public BEBPMEnumEntry(Component fieldName, Class<T> type, T value, Supplier<T> defaultValue,
                          Consumer<T> saveConsumer, Function<Enum, Component> nameProvider,
                          Supplier<Optional<Component[]>> tooltipLines) {
        super(fieldName, type, value, Component.translatable("text.cloth-config.reset_value"),
                defaultValue, saveConsumer, nameProvider, null, false);
        this.tooltipLines = tooltipLines;
    }

    @Override
    public Optional<Component[]> getTooltip(int mouseX, int mouseY) {
        return Optional.empty();
    }

    @Override
    public Optional<Component[]> getTooltip() {
        return tooltipLines == null ? Optional.empty() : tooltipLines.get();
    }

    @Override
    //? if >=26.1 {
    /*public void extractRenderState(GuiGraphicsExtractor graphics, int index, int y, int x, int entryWidth,
                                   int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.extractRenderState(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
    *///?} else {
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth,
                       int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
    //?}

        if (!isMouseInside(mouseX, mouseY, x, y, entryWidth, entryHeight)) {
            return;
        }

        getTooltip().ifPresent(lines -> {
            FormattedCharSequence[] wrapped = wrapLinesToScreen(lines);
            if (wrapped.length > 0) {
                addTooltip(BEBPMTooltips.belowLeft(wrapped, mouseX, mouseY));
            }
        });
    }
}
//?}
