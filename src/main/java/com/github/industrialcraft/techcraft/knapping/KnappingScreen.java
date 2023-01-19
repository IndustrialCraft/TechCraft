package com.github.industrialcraft.techcraft.knapping;

import com.github.industrialcraft.techcraft.TechCraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class KnappingScreen extends HandledScreen<KnappingScreenHandler> {
    private static HashMap<Item,ScreenRenderingData> renderingData = new HashMap<>();
    private record ScreenRenderingData(Identifier[] bitTexture, Identifier backgroundTexture){}
    public static void registerClientRenderingData(Item item, Identifier backgroundTexture, Identifier... bitTexture){
        renderingData.put(item, new ScreenRenderingData(bitTexture, backgroundTexture));
    }
    public KnappingScreen(KnappingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 9*16;
        this.backgroundHeight = 5*16;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ScreenRenderingData currentRenderingData = renderingData.get(handler.getKnappingInventory().getItemStack().getItem());
        RenderSystem.setShaderTexture(0, currentRenderingData==null?TechCraft.identifier("unknown"):currentRenderingData.backgroundTexture);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }
    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ScreenRenderingData currentRenderingData = renderingData.get(handler.getKnappingInventory().getItemStack().getItem());
        for(int x = 0;x < 5;x++){
            for(int y = 0;y < 5;y++){
                if(handler.getKnappingPattern().get(x, y) > 0){
                    RenderSystem.setShaderTexture(0, currentRenderingData.bitTexture[handler.getKnappingPattern().get(x, y)-1]);
                    drawTexture(matrices, x*16, y*16, 0, 0, 16, 16);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseX -= this.x;
        mouseY -= this.y;
        if(mouseX >= 0 && mouseX <= 5*16 && mouseY >= 0 && mouseY <= 5*16){
            int bitX = (int) (mouseX/16);
            int bitY = (int) (mouseY/16);
            this.client.interactionManager.clickButton(this.handler.syncId, bitX + (bitY*5));
        }
        return super.mouseClicked(mouseX + this.x, mouseY + this.y, button);
    }
}
