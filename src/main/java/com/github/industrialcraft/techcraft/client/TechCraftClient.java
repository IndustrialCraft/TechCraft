package com.github.industrialcraft.techcraft.client;

import com.github.industrialcraft.techcraft.TCBlocks;
import com.github.industrialcraft.techcraft.TCScreens;
import com.github.industrialcraft.techcraft.cables.CableConnectorBlockEntityRenderer;
import com.github.industrialcraft.techcraft.fluidbarrel.FluidBarrelBlockRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class TechCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TCScreens.onClientInitialize();
        BlockEntityRendererRegistry.register(TCBlocks.CABLE_CONNECTOR_BLOCK_ENTITY, CableConnectorBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(TCBlocks.FLUID_BARREL_BLOCK_ENTITY, FluidBarrelBlockRenderer::new);
    }
}
