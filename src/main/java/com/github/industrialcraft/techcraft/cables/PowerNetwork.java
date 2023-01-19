package com.github.industrialcraft.techcraft.cables;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.HashSet;

public class PowerNetwork extends SnapshotParticipant<Long> implements EnergyStorage {
    private HashSet<BlockPos> assigned;
    public PowerNetwork() {
        this.assigned = new HashSet<>();
    }
    public void add(World world, BlockPos pos){
        this.assigned.add(pos);
        if(world.getBlockEntity(pos) instanceof CableConnectorBlockEntity blockEntity){
            blockEntity.setPowerNetwork(this);
        }
    }
    public void remove(World world, BlockPos pos){
        this.assigned.remove(pos);
        if(world.getBlockEntity(pos) instanceof CableConnectorBlockEntity blockEntity){
            blockEntity.setPowerNetwork(null);
        }
    }
    public void merge(World world, PowerNetwork other){
        for(var pos : other.assigned){
            if(world.getBlockEntity(pos) instanceof CableConnectorBlockEntity blockEntity){
                blockEntity.setPowerNetwork(this);
                this.assigned.add(pos);
            }
        }
        other.assigned.clear();
    }

    public long amount = 0;
    public long capacity = 1000;
    public long maxInsert = 32, maxExtract = 32;

    @Override
    protected Long createSnapshot() {
        return amount;
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        amount = snapshot;
    }

    @Override
    public boolean supportsInsertion() {
        return maxInsert > 0;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notNegative(maxAmount);

        long inserted = Math.min(maxInsert, Math.min(maxAmount, capacity - amount));

        if (inserted > 0) {
            updateSnapshots(transaction);
            amount += inserted;
            return inserted;
        }

        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return maxExtract > 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notNegative(maxAmount);

        long extracted = Math.min(maxExtract, Math.min(maxAmount, amount));

        if (extracted > 0) {
            updateSnapshots(transaction);
            amount -= extracted;
            return extracted;
        }

        return 0;
    }
    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }
}
