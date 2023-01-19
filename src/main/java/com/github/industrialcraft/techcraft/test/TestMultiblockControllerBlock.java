package com.github.industrialcraft.techcraft.test;

import com.github.industrialcraft.techcraft.multiblock.MultiblockControllerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class TestMultiblockControllerBlock extends MultiblockControllerBlock {
    public TestMultiblockControllerBlock(Settings settings) {
        super(settings);
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
