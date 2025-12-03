package net.tracystacktrace.stackem.modloader.gui;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;

public class GuiButtonHover extends GuiButton {

    protected String hoverString;
    public boolean canDisplayInfo = false;

    public GuiButtonHover(int id, int x, int y, int w, int h, String s, String hoverString) {
        super(id, x, y, w, h, s);
        this.hoverString = hoverString;
    }

    public void drawHoverString(FontRenderer fontRenderer, int mouseX, int mouseY) {
        if (!this.canDisplayInfo || !this.enabled2) {
            return;
        }

        if (!this.mouseHovered(mouseX, mouseY)) {
            return;
        }

        final int string_width = fontRenderer.getStringWidth(this.hoverString);
        this.drawRect(mouseX + 3, mouseY + 3, mouseX + 6 + string_width + 4, mouseY + 6 + 4 + 11, -1073741824);
        fontRenderer.drawString(this.hoverString, mouseX + 6, mouseY + 6, 16777120);
    }

    protected boolean mouseHovered(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseX < this.xPosition + this.width && mouseY >= this.yPosition && mouseY < this.yPosition + this.height;
    }
}
