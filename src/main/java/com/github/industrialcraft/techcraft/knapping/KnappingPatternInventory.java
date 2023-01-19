package com.github.industrialcraft.techcraft.knapping;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

public class KnappingPatternInventory extends SimpleInventory {
    private KnappingPattern knappingPattern;
    private ItemStack itemStack;
    public KnappingPatternInventory() {
        super(1);
        this.knappingPattern = new KnappingPattern();
        this.itemStack = ItemStack.EMPTY;
    }
    public KnappingPattern getKnappingPattern() {
        return knappingPattern;
    }
    public void setKnappingPattern(KnappingPattern knappingPattern) {
        this.knappingPattern = knappingPattern;
    }
    public ItemStack getItemStack() {
        return itemStack;
    }
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        setStack(0, itemStack);
    }
}
