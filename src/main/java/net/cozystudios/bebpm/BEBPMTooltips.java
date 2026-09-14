package net.cozystudios.bebpm;

import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.math.Point;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.OrderedText;

public final class BEBPMTooltips {

    private static final int GAP = 4;
    private static final int BOX_INSET = 15;
    private static final int BORDER = 3;
    private static final int MIN_VISIBLE_LEFT = 4;

    private BEBPMTooltips() {
    }

    public static Tooltip belowLeft(OrderedText[] wrapped, int mouseX, int mouseY) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int width = 0;
        for (OrderedText line : wrapped) {
            width = Math.max(width, textRenderer.getWidth(line));
        }

        int visibleLeft = Math.max(mouseX - width - GAP - (BOX_INSET - BORDER * 2),
                MIN_VISIBLE_LEFT);
        return Tooltip.of(new Point(visibleLeft - (BOX_INSET - BORDER * 2), mouseY + GAP + BOX_INSET),
                wrapped);
    }
}
