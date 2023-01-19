package com.github.industrialcraft.techcraft;

import com.github.industrialcraft.techcraft.fluidbarrel.FluidBarrelItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class TCItems {
    public static Item LOG_PILE = new BlockItem(TCBlocks.LOG_PILE_BLOCK, new FabricItemSettings());
    public static Item CABLE_CONNECTOR = new BlockItem(TCBlocks.CABLE_CONNECTOR_BLOCK, new FabricItemSettings());
    public static Item FLUID_BARREL = new FluidBarrelItem(TCBlocks.FLUID_BARREL_BLOCK, new FabricItemSettings());
    public static Item FLUID_BARREL_LID = new Item(new FabricItemSettings());
    public static void onInitialize(){
        Registry.register(Registries.ITEM, TechCraft.identifier("log_pile"), LOG_PILE);
        Registry.register(Registries.ITEM, TechCraft.identifier("cable_connector"), CABLE_CONNECTOR);
        Registry.register(Registries.ITEM, TechCraft.identifier("fluid_barrel"), FLUID_BARREL);
        Registry.register(Registries.ITEM, TechCraft.identifier("fluid_barrel_lid"), FLUID_BARREL_LID);
    }
}
