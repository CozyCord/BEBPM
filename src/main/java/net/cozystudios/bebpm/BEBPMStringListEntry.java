package net.cozystudios.bebpm;

import me.shedaniel.clothconfig2.gui.entries.BaseListCell;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
//? if >=26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
*///?} else {
import net.minecraft.client.gui.GuiGraphics;
//?}
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BEBPMStringListEntry extends StringListListEntry {

    private static final int FIRST_CELL_OFFSET = 24;
    private static final int CELL_INSET = 12;

    private static final int ROW_HEIGHT = 12;
    private static final int ROW_PAD_LEFT = 2;

    private static final int ROW_STRIP = 0x14FFFFFF;
    private static final int ROW_UNDERLINE = 0x40FFFFFF;

    private final Supplier<Optional<Component[]>> tooltipLines;

    public BEBPMStringListEntry(Component fieldName, List<String> value, Supplier<List<String>> defaultValue,
                                Consumer<List<String>> saveConsumer,
                                Supplier<Optional<Component[]>> tooltipLines) {
        super(fieldName, value, false, null, saveConsumer, defaultValue,
                Component.translatable("text.cloth-config.reset_value"), true);
        this.tooltipLines = tooltipLines;

        setCellErrorSupplier(entry -> entry == null || entry.trim().isEmpty()
                || BEBPMLineOfSight.isValidEntry(entry)
                ? Optional.empty()
                : Optional.of(Component.translatable("bebpm.config.decor_permeable.invalid")));
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
        if (isExpanded()) {
            drawRows(graphics, y, x, entryWidth);
        }

        super.extractRenderState(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
    *///?} else {
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth,
                       int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        if (isExpanded()) {
            drawRows(graphics, y, x, entryWidth);
        }

        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
    //?}

        if (!isMouseInside(mouseX, mouseY, x, y, entryWidth, FIRST_CELL_OFFSET)) {
            return;
        }

        getTooltip().ifPresent(lines -> {
            FormattedCharSequence[] wrapped = wrapLinesToScreen(lines);
            if (wrapped.length > 0) {
                addTooltip(BEBPMTooltips.belowLeft(wrapped, mouseX, mouseY));
            }
        });
    }

    //? if >=26.1 {
    /*private void drawRows(GuiGraphicsExtractor graphics, int y, int x, int entryWidth) {
    *///?} else {
    private void drawRows(GuiGraphics graphics, int y, int x, int entryWidth) {
    //?}
        int left = x - ROW_PAD_LEFT;
        int right = x + entryWidth - CELL_INSET;
        int cellY = y + FIRST_CELL_OFFSET;

        for (BaseListCell cell : cells) {
            graphics.fill(left, cellY, right, cellY + ROW_HEIGHT, ROW_STRIP);
            graphics.fill(left, cellY + ROW_HEIGHT, right, cellY + ROW_HEIGHT + 1, ROW_UNDERLINE);
            cellY += cell.getCellHeight();
        }
    }
}
