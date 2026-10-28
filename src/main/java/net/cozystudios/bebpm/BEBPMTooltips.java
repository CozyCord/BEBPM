//? if cloth {
package net.cozystudios.bebpm;

import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.math.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.util.FormattedCharSequence;

public final class BEBPMTooltips {

    private static final int GAP = 4;
    private static final int BOX_INSET = 15;
    private static final int BORDER = 3;
    private static final int MIN_VISIBLE_LEFT = 4;

    private BEBPMTooltips() {
    }

    public static Tooltip belowLeft(FormattedCharSequence[] wrapped, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        int width = 0;
        for (FormattedCharSequence line : wrapped) {
            width = Math.max(width, font.width(line));
        }

        int visibleLeft = Math.max(mouseX - width - GAP - (BOX_INSET - BORDER * 2),
                MIN_VISIBLE_LEFT);
        return Tooltip.of(new Point(visibleLeft - (BOX_INSET - BORDER * 2), mouseY + GAP + BOX_INSET),
                wrapped);
    }
}
//?}
