package com.github.industrialcraft.techcraft.knapping;

import com.github.industrialcraft.techcraft.TCScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class KnappingScreenHandler extends ScreenHandler {
    private KnappingPatternInventory knappingInventory;
    private SimpleInventory outputInventory;
    private PropertyDelegate patternSyncer = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return knappingInventory.getKnappingPattern().get(index);
        }
        @Override
        public void set(int index, int value) {
            knappingInventory.getKnappingPattern().set(index, value);
        }
        @Override
        public int size() {
            return 25;
        }
    };
    public KnappingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(TCScreens.KNAPPING_SCREEN_HANDLER, syncId);
        this.outputInventory = new SimpleInventory(1);
        this.knappingInventory = new KnappingPatternInventory();
        addSlot(new Slot(outputInventory, 0, 6*16+10, (int) (2.5f*16)){
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return false;
            }
        });
        addSlot(new Slot(knappingInventory, 0, 5*16+10, (int) (2.5f*16)){
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return false;
            }
        });
        addProperties(patternSyncer);
    }
    public KnappingScreenHandler(int syncId, PlayerInventory playerInventory, int filler, ItemStack input){
        this(syncId, playerInventory);
        this.knappingInventory.getKnappingPattern().fillPattern(filler);
        this.knappingInventory.setItemStack(input);
        onPatternModified(playerInventory.player.world);
    }
    public void onPatternModified(World world){
        Optional<ItemStack> match = world.getRecipeManager()
                .getFirstMatch(KnappingRecipe.KnappingRecipeType.INSTANCE, knappingInventory, world).map(KnappingRecipe::getOutput);
        outputInventory.setStack(0, match.orElse(ItemStack.EMPTY));
    }
    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if(id >= 0 && id <= 24) {
            knappingInventory.getKnappingPattern().chip(id % 5, id / 5);
            onPatternModified(player.world);
        }
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }
    @Override
    public boolean canUse(PlayerEntity player) {
        return knappingInventory.getKnappingPattern().getBitsPresent() > 0;
    }
    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        if(player instanceof ServerPlayerEntity serverPlayerEntity){
            if(knappingInventory.getItemStack().isEmpty())
                return;
            onPatternModified(player.world);
            if(outputInventory.getStack(0).isEmpty())
                serverPlayerEntity.getInventory().offerOrDrop(knappingInventory.getItemStack());
            else
                serverPlayerEntity.getInventory().offerOrDrop(outputInventory.getStack(0).copy());
            knappingInventory.getKnappingPattern().fillPattern(0);
            onPatternModified(player.world);
            knappingInventory.setItemStack(ItemStack.EMPTY);
        }
    }
    public KnappingPatternInventory getKnappingInventory() {
        return knappingInventory;
    }
    public KnappingPattern getKnappingPattern() {
        return knappingInventory.getKnappingPattern();
    }
}
