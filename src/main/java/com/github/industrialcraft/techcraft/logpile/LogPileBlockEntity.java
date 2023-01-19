package com.github.industrialcraft.techcraft.logpile;

import com.github.industrialcraft.techcraft.TCBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LogPileBlockEntity extends BlockEntity {
    private int destroyTimer;
    private int finishTimer;
    public LogPileBlockEntity(BlockPos pos, BlockState state) {
        super(TCBlocks.LOG_PILE_BLOCK_ENTITY, pos, state);
        this.destroyTimer = 20*5;
        this.finishTimer = 20*60;
    }
    public static void tick(World world, BlockPos pos, BlockState state, LogPileBlockEntity be) {
        if(be.finishTimer <= 0){
            world.setBlockState(pos, TCBlocks.CHARCOAL_PILE_BLOCK.getDefaultState());
            return;
        }
        if(be.destroyTimer <= 0){
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }
        if(state.get(LogPileBlock.ON_FIRE)){
            if(state.get(LogPileBlock.AIRLESS)){
                be.finishTimer--;
            } else {
                be.destroyTimer--;
            }
            be.markDirty();
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("finishTime", finishTimer);
        nbt.putInt("destroyTime", destroyTimer);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.finishTimer = nbt.getInt("finishTime");
        this.destroyTimer = nbt.getInt("destroyTime");
    }
}
