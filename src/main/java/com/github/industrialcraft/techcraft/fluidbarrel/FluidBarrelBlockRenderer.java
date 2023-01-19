package com.github.industrialcraft.techcraft.fluidbarrel;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;

public class FluidBarrelBlockRenderer implements BlockEntityRenderer<FluidBarrelBlockEntity> {
    public FluidBarrelBlockRenderer(BlockEntityRendererFactory.Context ctx) {

    }
    @Override
    public void render(FluidBarrelBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        float height = ((float)entity.fluidStorage.amount) / (FluidConstants.BLOCK*FluidBarrelBlockEntity.BUCKETS_STORED);
        FluidVariant fluidVariant = entity.fluidStorage.variant;
        if(fluidVariant != null && height > 0) {
            matrices.push();
            Sprite sprite = FluidVariantRendering.getSprite(fluidVariant);
            int color = FluidVariantRendering.getColor(fluidVariant, MinecraftClient.getInstance().world, entity.getPos());
            VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
            int alpha = ((color >> 24 & 0xFF) > 0) ? color >> 24 & 0xFF : 0xFF;
            int red = color >> 16 & 0xFF;
            int green = color >> 8 & 0xFF;
            int blue = color >> 0 & 0xFF;
            int luminance = FluidVariantAttributes.getLuminance(fluidVariant);
            int sky = luminance >> 16 & 0xFFFF;
            int block = luminance >> 0 & 0xFFFF;
            buffer.vertex(matrices.peek().getPositionMatrix(), 0, height, 1).color(red, green, blue, alpha).texture(sprite.getMinU(), sprite.getMaxV()).light(block, sky).normal(0, -1, 0).next();
            buffer.vertex(matrices.peek().getPositionMatrix(), 1, height, 1).color(red, green, blue, alpha).texture(sprite.getMaxU(), sprite.getMaxV()).light(block, sky).normal(0, -1, 0).next();
            buffer.vertex(matrices.peek().getPositionMatrix(), 1, height, 0).color(red, green, blue, alpha).texture(sprite.getMaxU(), sprite.getMinV()).light(block, sky).normal(0, -1, 0).next();
            buffer.vertex(matrices.peek().getPositionMatrix(), 0, height, 0).color(red, green, blue, alpha).texture(sprite.getMinU(), sprite.getMinV()).light(block, sky).normal(0, -1, 0).next();
            matrices.pop();
        }
    }

}
