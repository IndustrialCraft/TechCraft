package com.github.industrialcraft.techcraft.cables;

import com.github.industrialcraft.techcraft.TCBlocks;
import com.github.industrialcraft.techcraft.logpile.LogPileBlock;
import com.github.industrialcraft.techcraft.logpile.LogPileBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

import java.util.ArrayList;
import java.util.HashSet;

public class CableConnectorBlockEntity extends BlockEntity {
    private HashSet<BlockPos> connections;
    private PowerNetwork powerNetwork;
    public CableConnectorBlockEntity(BlockPos pos, BlockState state) {
        super(TCBlocks.CABLE_CONNECTOR_BLOCK_ENTITY, pos, state);
        this.connections = new HashSet<>();
    }
    public static void tick(World world, BlockPos pos, BlockState state, CableConnectorBlockEntity be) {
        if(be.powerNetwork == null) {
            PowerNetwork powerNetwork = new PowerNetwork();
            powerNetwork.add(world, pos);
            for(var connection : be.connections){
                if(world.getBlockEntity(connection) instanceof CableConnectorBlockEntity blockEntity){
                    if(blockEntity.powerNetwork != null){
                        powerNetwork.merge(world, blockEntity.powerNetwork);
                    }
                }
            }
        } else {
            Direction direction = state.get(Properties.FACING);
            EnergyStorage storage = EnergyStorage.SIDED.find(world, pos.offset(direction), direction.getOpposite());
            EnergyStorageUtil.move(be.getPowerNetwork(), storage, 32, null);
        }
    }
    public PowerNetwork getPowerNetwork() {
        return powerNetwork;
    }
    public void setPowerNetwork(PowerNetwork powerNetwork) {
        this.powerNetwork = powerNetwork;
    }
    public void connect(BlockPos other){
        if(other.equals(pos))
            return;
        if(connections.contains(other))
            return;
        if(world.getBlockEntity(other) instanceof CableConnectorBlockEntity blockEntity){
            connections.add(other);
            blockEntity.connections.add(pos);
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
            world.updateListeners(other, blockEntity.getCachedState(), blockEntity.getCachedState(), Block.NOTIFY_LISTENERS);
            if(blockEntity.powerNetwork != this.powerNetwork)
                this.powerNetwork.merge(world, blockEntity.powerNetwork);
        }
    }
    public void disconnect(BlockPos other){
        if(!connections.contains(other))
            return;
        if(world.getBlockEntity(other) instanceof CableConnectorBlockEntity blockEntity){
            connections.remove(other);
            blockEntity.connections.remove(pos);
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
            world.updateListeners(other, blockEntity.getCachedState(), blockEntity.getCachedState(), Block.NOTIFY_LISTENERS);

            HashSet<BlockPos> checked = new HashSet<>();
            ArrayList<BlockPos> toCheck = new ArrayList<>();
            toCheck.add(other);
            while(toCheck.size()>0){
                BlockPos checking = toCheck.get(0);
                if(checking.equals(pos))
                    return;
                toCheck.remove(0);
                if(checked.contains(checking))
                    continue;
                if(world.getBlockEntity(checking) instanceof CableConnectorBlockEntity checkingEntity){
                    toCheck.addAll(checkingEntity.connections);
                }
                checked.add(checking);
            }
            if(checked.size() > 0){
                PowerNetwork newPowerNet = new PowerNetwork();
                for(var block : checked){
                    powerNetwork.remove(world, block);
                    newPowerNet.add(world, block);
                }
            }
        }
    }
    public void onDestroy(){
        for(var connection : new ArrayList<>(connections)){
            disconnect(connection);
        }
    }

    private NbtList connectionsToNbt(){
        NbtList nbtConnections = new NbtList();
        for(var connection : connections){
            nbtConnections.add(new NbtIntArray(new int[]{connection.getX(),connection.getY(),connection.getZ()}));
        }
        return nbtConnections;
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("connections", connectionsToNbt());
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        connections.clear();
        NbtList nbtConnections = nbt.getList("connections", 11);
        for(var connection : nbtConnections){
            NbtIntArray coords = (NbtIntArray) connection;
            connections.add(new BlockPos(coords.get(0).intValue(), coords.get(1).intValue(), coords.get(2).intValue()));
        }
    }
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.put("connections", connectionsToNbt());
        return nbt;
    }
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    public HashSet<BlockPos> getConnections() {
        return connections;
    }
}
