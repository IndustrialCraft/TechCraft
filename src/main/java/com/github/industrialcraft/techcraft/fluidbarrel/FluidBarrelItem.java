package com.github.industrialcraft.techcraft.fluidbarrel;

import com.github.industrialcraft.techcraft.TCBlocks;
import com.github.industrialcraft.techcraft.TCItems;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidBarrelItem extends BlockItem {
    public FluidBarrelItem(Block block, Settings settings) {
        super(block, settings);
    }
    public static void setFromBlock(ItemStack stack, BlockState state, FluidBarrelBlockEntity be){
        if(!stack.isOf(TCItems.FLUID_BARREL))
            throw new IllegalArgumentException("stack must be fluid barrel");
        boolean hasLid = state.get(FluidBarrelBlock.HAS_LID);
        if(hasLid && be.fluidStorage.variant != null) {
            setBlockEntityNbt(stack, TCBlocks.FLUID_BARREL_BLOCK_ENTITY, be.toInitialChunkDataNbt());
        }
        if(hasLid){
            NbtCompound blockStateTag = new NbtCompound();
            blockStateTag.put(FluidBarrelBlock.HAS_LID.getName(), NbtString.of("true"));
            stack.setSubNbt("BlockStateTag", blockStateTag);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        NbtCompound blockEntityTag = stack.getSubNbt("BlockEntityTag");
        if(blockEntityTag != null){
            long amount = blockEntityTag.getLong("amount");
            FluidVariant variant = FluidVariant.fromNbt(blockEntityTag.getCompound("fluidVariant"));
            tooltip.add(Text.literal(variant.getFluid().getRegistryEntry().registryKey().getValue() + "-" + (amount/81) + "mB"));
        }
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        return super.place(context);
    }
}
