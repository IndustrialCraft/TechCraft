package com.github.industrialcraft.techcraft.fluidbarrel;

import com.github.industrialcraft.techcraft.TCItems;
import com.github.industrialcraft.techcraft.mixin.BucketItemAccessor;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FluidBarrelBlock extends BlockWithEntity {
    public static final BooleanProperty HAS_LID = BooleanProperty.of("has_lid");

    public FluidBarrelBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(HAS_LID, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient)
            return ActionResult.CONSUME;
        ItemStack handStack = player.getStackInHand(hand);
        boolean hasLid = state.get(HAS_LID);
        boolean creative = player.isCreative();
        if(player.isSneaking() && handStack.isEmpty() && hasLid){
            world.setBlockState(pos, state.with(HAS_LID, false));
            if(!creative)
                player.getInventory().offerOrDrop(new ItemStack(TCItems.FLUID_BARREL_LID));
            return ActionResult.SUCCESS;
        }
        if(handStack.isOf(TCItems.FLUID_BARREL_LID) && !hasLid){
            world.setBlockState(pos, state.with(HAS_LID, true));
            if(!creative)
                handStack.decrement(1);
            return ActionResult.SUCCESS;
        }
        if(handStack.getItem() instanceof BucketItem bucketItem){
            if (!hasLid && world.getBlockEntity(pos) instanceof FluidBarrelBlockEntity be) {
                Fluid fluid = ((BucketItemAccessor) bucketItem).getFluid();
                if(fluid == Fluids.EMPTY){
                    if(be.fluidStorage.variant == null)
                        return ActionResult.CONSUME;
                    try (Transaction transaction = Transaction.openOuter()) {
                        long extracted = be.fluidStorage.extract(be.fluidStorage.variant, FluidConstants.BUCKET, transaction);
                        if (extracted == FluidConstants.BUCKET) {
                            if (!creative)
                                handStack.decrement(1);
                            player.getInventory().offerOrDrop(new ItemStack(be.fluidStorage.variant.getFluid().getBucketItem()));
                            transaction.commit();
                            return ActionResult.SUCCESS;
                        } else {
                            return ActionResult.CONSUME;
                        }
                    }
                } else {
                    try (Transaction transaction = Transaction.openOuter()) {
                        long inserted = be.fluidStorage.insert(FluidVariant.of(fluid), FluidConstants.BUCKET, transaction);
                        if (inserted == FluidConstants.BUCKET) {
                            if (!creative) {
                                handStack.decrement(1);
                                player.getInventory().offerOrDrop(new ItemStack(Items.BUCKET));
                            }
                            transaction.commit();
                            return ActionResult.SUCCESS;
                        } else {
                            return ActionResult.CONSUME;
                        }
                    }
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        player.incrementStat(Stats.MINED.getOrCreateStat(this));
        player.addExhaustion(0.005F);
        if (world instanceof ServerWorld && blockEntity instanceof FluidBarrelBlockEntity fluidBarrelBlockEntity) {
            ItemStack stackx = new ItemStack(TCItems.FLUID_BARREL);
            FluidBarrelItem.setFromBlock(stackx, state, fluidBarrelBlockEntity);
            dropStack(world, pos, stackx);
            state.onStacksDropped((ServerWorld)world, pos, stack, true);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_LID);
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidBarrelBlockEntity(pos, state);
    }
}
