package com.github.industrialcraft.techcraft.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class MultiblockControllerBlock extends BlockWithEntity {
    protected MultiblockControllerBlock(Settings settings) {
        super(settings);
    }
}
