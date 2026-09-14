package net.cozystudios.bebpm;

import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import me.shedaniel.math.Point;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BEBPMSliderEntry extends IntegerSliderEntry {

    private static final int GAP = 4;
    private static final int BOX_INSET = 15;
    private static final int MIN_VISIBLE_LEFT = 4;

    private final Supplier<Optional<Text[]>> tooltipLines;

    public BEBPMSliderEntry(Text fieldName, int min, int max, int value,
                            Supplier<Integer> defaultValue, Consumer<Integer> saveConsumer,
                            Supplier<Optional<Text[]>> tooltipLines) {
        super(fieldName, min, max, value, Text.translatable("text.cloth-config.reset_value"),
                defaultValue, saveConsumer, null, false);
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
            if (wrapped.length == 0) {
                return;
            }

            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            int width = 0;
            for (OrderedText line : wrapped) {
                width = Math.max(width, textRenderer.getWidth(line));
            }

            int visibleLeft = Math.max(mouseX - width - GAP - (BOX_INSET - 9), MIN_VISIBLE_LEFT);
            int pointX = visibleLeft - 9;
            int pointY = mouseY + GAP + BOX_INSET;

            addTooltip(Tooltip.of(new Point(pointX, pointY), wrapped));
        });
    }
}
