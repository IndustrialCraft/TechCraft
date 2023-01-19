package com.github.industrialcraft.techcraft.logpile;

import com.github.industrialcraft.techcraft.TCBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class LogPileBlock extends BlockWithEntity {
    public static final BooleanProperty AIRLESS = BooleanProperty.of("airless");
    public static final BooleanProperty ON_FIRE = BooleanProperty.of("on_fire");

    public LogPileBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(AIRLESS, false).with(ON_FIRE, false));
    }
    private static BlockState getNextState(BlockState currentState, BlockView world, BlockPos pos) {
        boolean nearFire = false;
        boolean solidOnly = true;
        BlockPos.Mutable mutable = pos.mutableCopy();
        for(int i = 0; i < Direction.values().length; i++) {
            Direction direction = Direction.values()[i];
            mutable.set(pos, direction);
            BlockState state = world.getBlockState(mutable);
            if(state.isOf(Blocks.FIRE) || (state.getBlock() instanceof LogPileBlock && state.get(ON_FIRE)))
                nearFire = true;
            if(!state.isFullCube(world, mutable))
                solidOnly = false;
        }
        if(nearFire)
            currentState = currentState.with(ON_FIRE, true);
        currentState = currentState.with(AIRLESS, solidOnly);
        return currentState;
    }
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return getNextState(state, world, pos);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if(state.get(ON_FIRE)) {
            boolean airless = state.get(AIRLESS);
            if(airless && random.nextInt(5)!=0)
                return;
            for(int i = 0;i < (airless?1:3);i++)
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5 + random.nextFloat()*0.3, pos.getY() + 1.5 + random.nextFloat()*0.3, pos.getZ() + 0.5 + random.nextFloat()*0.3, random.nextFloat()*0.1, 0.4 + random.nextFloat()*0.1, random.nextFloat()*0.1);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AIRLESS);
        builder.add(ON_FIRE);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LogPileBlockEntity(pos, state);
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, TCBlocks.LOG_PILE_BLOCK_ENTITY, LogPileBlockEntity::tick);
    }
}
