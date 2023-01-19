package com.github.industrialcraft.techcraft.cables;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class CableConnectorBlockEntityRenderer implements BlockEntityRenderer<CableConnectorBlockEntity> {
    public CableConnectorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(CableConnectorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());
        matrices.translate(-entity.getPos().getX(), -entity.getPos().getY(), -entity.getPos().getZ());
        for(var connection : entity.getConnections()){
            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), entity.getPos().getX()+.5f, entity.getPos().getY()+.5f, entity.getPos().getZ()+.5f).color(0, 0, 0, 255).normal(0, 0, 0).next();
            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), connection.getX()+.5f, connection.getY()+.5f, connection.getZ()+.5f).color(0, 0, 0, 255).normal(0, 0, 0).next();
        }
        matrices.pop();
    }
}
