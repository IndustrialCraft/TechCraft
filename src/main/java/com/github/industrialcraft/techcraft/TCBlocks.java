package com.github.industrialcraft.techcraft;

import com.github.industrialcraft.techcraft.cables.CableConnectorBlock;
import com.github.industrialcraft.techcraft.cables.CableConnectorBlockEntity;
import com.github.industrialcraft.techcraft.fluidbarrel.FluidBarrelBlock;
import com.github.industrialcraft.techcraft.fluidbarrel.FluidBarrelBlockEntity;
import com.github.industrialcraft.techcraft.logpile.LogPileBlock;
import com.github.industrialcraft.techcraft.logpile.LogPileBlockEntity;
import com.github.industrialcraft.techcraft.test.TestMultiblockControllerBlock;
import com.github.industrialcraft.techcraft.test.TestMultiblockControllerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import team.reborn.energy.api.EnergyStorage;

public class TCBlocks {
    public static Block LOG_PILE_BLOCK = new LogPileBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    public static BlockEntityType<LogPileBlockEntity> LOG_PILE_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(LogPileBlockEntity::new, LOG_PILE_BLOCK).build();

    public static Block CHARCOAL_PILE_BLOCK = new Block(FabricBlockSettings.of(Material.AGGREGATE).strength(0.1f).sounds(BlockSoundGroup.WOOD));

    public static Block CABLE_CONNECTOR_BLOCK = new CableConnectorBlock(FabricBlockSettings.of(Material.METAL));
    public static BlockEntityType<CableConnectorBlockEntity> CABLE_CONNECTOR_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(CableConnectorBlockEntity::new, CABLE_CONNECTOR_BLOCK).build();

    public static Block TEST_MULTIBLOCK_CONTROLLER = new TestMultiblockControllerBlock(FabricBlockSettings.of(Material.METAL));
    public static BlockEntityType<TestMultiblockControllerBlockEntity> TEST_MULTIBLOCK_CONTROLLER_ENTITY = FabricBlockEntityTypeBuilder.create(TestMultiblockControllerBlockEntity::new, TEST_MULTIBLOCK_CONTROLLER).build();

    public static Block FLUID_BARREL_BLOCK = new FluidBarrelBlock(FabricBlockSettings.of(Material.WOOD).strength(2.0f));
    public static BlockEntityType<FluidBarrelBlockEntity> FLUID_BARREL_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(FluidBarrelBlockEntity::new, FLUID_BARREL_BLOCK).build();

    public static void onInitialize(){
        Registry.register(Registries.BLOCK, TechCraft.identifier("log_pile"), LOG_PILE_BLOCK);
        Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                TechCraft.identifier("log_pile"),
                LOG_PILE_BLOCK_ENTITY
        );

        Registry.register(Registries.BLOCK, TechCraft.identifier("charcoal_pile"), CHARCOAL_PILE_BLOCK);

        Registry.register(Registries.BLOCK, TechCraft.identifier("cable_connector"), CABLE_CONNECTOR_BLOCK);
        Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                TechCraft.identifier("cable_connector"),
                CABLE_CONNECTOR_BLOCK_ENTITY
        );
        EnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> myBlockEntity.getPowerNetwork(), CABLE_CONNECTOR_BLOCK_ENTITY);

        Registry.register(Registries.BLOCK, TechCraft.identifier("test_multiblock_controller"), TEST_MULTIBLOCK_CONTROLLER);
        Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                TechCraft.identifier("test_multiblock_controller"),
                TEST_MULTIBLOCK_CONTROLLER_ENTITY
        );

        Registry.register(Registries.BLOCK, TechCraft.identifier("fluid_barrel"), FLUID_BARREL_BLOCK);
        Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                TechCraft.identifier("fluid_barrel"),
                FLUID_BARREL_BLOCK_ENTITY
        );
    }
}
