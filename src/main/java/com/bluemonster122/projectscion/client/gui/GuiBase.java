package com.bluemonster122.projectscion.client.gui;

import com.bluemonster122.projectscion.ModInfo;
import com.bluemonster122.projectscion.common.util.GuiHelper;
import com.bluemonster122.projectscion.common.util.OpenGLHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public abstract class GuiBase extends GuiContainer {
    protected int colorBackground = new Color(56, 55, 69, 224).hashCode();
    protected int colorBorder = new Color(48, 41, 69).hashCode();
    protected int colorFont = new Color(255, 255, 255).hashCode();
    protected int colorErrorFont = new Color(255, 64, 64).hashCode();
    protected int colorProgressBackground = new Color(64, 64, 255, 128).hashCode();
    protected int colorProgressBackgroundGood = new Color(0, 170, 0).hashCode();
    protected int colorProgressBackgroundWarn = new Color(255, 170, 0).hashCode();
    protected int colorProgressBackgroundBad = new Color(255, 85, 85).hashCode();
    protected int colorXPGreen = new Color(128, 255, 32).hashCode();
    GuiHelper guiHelper;

    public GuiBase(Container container) {
        super(container);
        guiHelper = new GuiHelper();
    }

    public void drawTooltip(int mouseX, int mouseY, int forceWidth, String message) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int[][] savedGLState = OpenGLHelper.saveGLState(new int[]{GL11.GL_ALPHA_TEST, GL11.GL_LIGHTING});

        GL11.glPushMatrix();


        guiHelper.drawWindowWithBorder(mouseX, mouseY, forceWidth, 10, colorBackground, colorBorder);
        guiHelper.drawCenteredStringWithShadow(mouseX, mouseY, forceWidth, "Hello World", colorFont);

        GL11.glPopMatrix();

        OpenGLHelper.restoreGLState(savedGLState);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public abstract void drawBG(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public abstract void drawFG(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public void bindTexture(String base, String file) {
        ResourceLocation resourceLocation = new ResourceLocation(base, "textures/" + file);
        this.mc.getTextureManager().bindTexture(resourceLocation);
    }

    public void bindTexture(String file) {
        ResourceLocation resourceLocation = new ResourceLocation(ModInfo.MOD_ID, "textures/" + file);
        this.mc.getTextureManager().bindTexture(resourceLocation);
    }

    protected final void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int ox = this.guiLeft;
        int oy = this.guiTop;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawBG(ox, oy, x, y);
    }

    protected final void drawGuiContainerForegroundLayer(int x, int y) {
        int ox = this.guiLeft;
        int oy = this.guiTop;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawFG(ox, oy, x, y);
    }

    protected Slot getSlot(int mouseX, int mouseY) {
        for (int j1 = 0; j1 < this.inventorySlots.inventorySlots.size(); j1++) {
            Slot slot = this.inventorySlots.inventorySlots.get(j1);
            if (isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY)) {
                return slot;
            }
        }
        return null;
    }

    protected void drawTransparentIconEmpty(Slot slot, ItemStack itemStack) {
        if (slot.getHasStack())
            return;

        drawTransparentIcon(slot, itemStack);
    }

    protected void drawIconEmpty(Slot slot, ItemStack itemStack) {
        if (slot.getHasStack())
            return;

        drawIcon(slot, itemStack);
    }

    protected void drawTransparentIcon(Slot slot, ItemStack itemStack) {
        ItemStack displayStack = itemStack.copy();
        if (itemStack.getItemDamage() == Short.MAX_VALUE)
            displayStack.setItemDamage(0);

        guiHelper.drawItemStack(displayStack, slot.xDisplayPosition + guiLeft, slot.yDisplayPosition + guiTop, this.itemRender, true);
    }

    protected void drawIcon(Slot slot, ItemStack itemStack) {
        ItemStack displayStack = itemStack.copy();
        if (itemStack.getItemDamage() == Short.MAX_VALUE)
            displayStack.setItemDamage(0);

        guiHelper.drawItemStack(itemStack, slot.xDisplayPosition + guiLeft, slot.yDisplayPosition + guiTop, this.itemRender, false);
    }

    public void renderToolTip(List<String> messages, int x, int y) {
        this.drawHoveringText(messages, x, y, fontRendererObj);
    }
}
