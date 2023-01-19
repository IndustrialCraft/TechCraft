package com.github.industrialcraft.techcraft.test;

import com.github.industrialcraft.techcraft.TCBlocks;
import com.github.industrialcraft.techcraft.multiblock.MultiblockControllerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class TestMultiblockControllerBlockEntity extends MultiblockControllerBlockEntity {
    public TestMultiblockControllerBlockEntity(BlockPos pos, BlockState state) {
        super(TCBlocks.TEST_MULTIBLOCK_CONTROLLER_ENTITY, pos, state);
    }
}
