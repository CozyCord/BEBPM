package net.cozystudios.bebpm;

import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import me.shedaniel.math.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
//? if >=26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
*///?} else {
import net.minecraft.client.gui.GuiGraphics;
//?}
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BEBPMSliderEntry extends IntegerSliderEntry {

    private static final int GAP = 4;
    private static final int BOX_INSET = 15;
    private static final int MIN_VISIBLE_LEFT = 4;

    private final Supplier<Optional<Component[]>> tooltipLines;

    public BEBPMSliderEntry(Component fieldName, int min, int max, int value,
                            Supplier<Integer> defaultValue, Consumer<Integer> saveConsumer,
                            Supplier<Optional<Component[]>> tooltipLines) {
        super(fieldName, min, max, value, Component.translatable("text.cloth-config.reset_value"),
                defaultValue, saveConsumer, null, false);
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
            if (wrapped.length == 0) {
                return;
            }

            Font font = Minecraft.getInstance().font;
            int width = 0;
            for (FormattedCharSequence line : wrapped) {
                width = Math.max(width, font.width(line));
            }

            int visibleLeft = Math.max(mouseX - width - GAP - (BOX_INSET - 9), MIN_VISIBLE_LEFT);
            int pointX = visibleLeft - 9;
            int pointY = mouseY + GAP + BOX_INSET;

            addTooltip(Tooltip.of(new Point(pointX, pointY), wrapped));
        });
    }
}
