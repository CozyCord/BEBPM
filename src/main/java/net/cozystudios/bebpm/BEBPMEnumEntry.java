package net.cozystudios.bebpm;

import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BEBPMEnumEntry<T extends Enum<?>> extends EnumListEntry<T> {

    private final Supplier<Optional<Text[]>> tooltipLines;

    public BEBPMEnumEntry(Text fieldName, Class<T> type, T value, Supplier<T> defaultValue,
                          Consumer<T> saveConsumer, Function<Enum, Text> nameProvider,
                          Supplier<Optional<Text[]>> tooltipLines) {
        super(fieldName, type, value, Text.translatable("text.cloth-config.reset_value"),
                defaultValue, saveConsumer, nameProvider, null, false);
        this.tooltipLines = tooltipLines;
    }

    @Override
    public Optional<Text[]> getTooltip(int mouseX, int mouseY) {
        return Optional.empty();
    }

    @Override
    public Optional<Text[]> getTooltip() {
        return tooltipLines == null ? Optional.empty() : tooltipLines.get();
    }

    @Override
    public void render(DrawContext graphics, int index, int y, int x, int entryWidth,
                       int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);

        if (!isMouseInside(mouseX, mouseY, x, y, entryWidth, entryHeight)) {
            return;
        }

        getTooltip().ifPresent(lines -> {
            OrderedText[] wrapped = wrapLinesToScreen(lines);
            if (wrapped.length > 0) {
                addTooltip(BEBPMTooltips.belowLeft(wrapped, mouseX, mouseY));
            }
        });
    }
}
