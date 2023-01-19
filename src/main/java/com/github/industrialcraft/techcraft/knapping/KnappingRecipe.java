package com.github.industrialcraft.techcraft.knapping;

import com.github.industrialcraft.techcraft.TechCraft;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class KnappingRecipe implements Recipe<KnappingPatternInventory> {
    private final Ingredient input;
    private final KnappingPattern pattern;
    private final ItemStack outputStack;
    private final Identifier id;
    public KnappingRecipe(Ingredient input, KnappingPattern pattern, ItemStack outputStack, Identifier id) {
        this.input = input;
        this.pattern = pattern;
        this.outputStack = outputStack;
        this.id = id;
    }

    @Override
    public boolean matches(KnappingPatternInventory inventory, World world) {
        return inventory.getKnappingPattern().equals(pattern) && input.test(inventory.getItemStack());
    }
    @Override
    public ItemStack getOutput() {
        return outputStack;
    }
    @Override
    public Identifier getId() {
        return id;
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return KnappingRecipeSerializer.INSTANCE;
    }
    public static class KnappingRecipeSerializer implements RecipeSerializer<KnappingRecipe>{
        private KnappingRecipeSerializer() {
        }
        public static final KnappingRecipeSerializer INSTANCE = new KnappingRecipeSerializer();
        public static final Identifier ID = TechCraft.identifier("knapping_recipe");

        @Override
        public KnappingRecipe read(Identifier id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            Item outputItem = Registries.ITEM.get(new Identifier(json.get("outputItem").getAsString()));
            ItemStack outputStack = new ItemStack(outputItem, Math.max(json.get("outputAmount").getAsInt(), 1));
            KnappingPattern knappingPattern = new KnappingPattern();
            JsonArray pattern = json.get("pattern").getAsJsonArray();
            if(pattern.size() != 5)
                throw new IllegalStateException("pattern must be 5x5");
            for(int i = 0;i < 5;i++){
                String row = pattern.get(i).getAsString();
                if(row.length() != 5)
                    throw new IllegalStateException("pattern must be 5x5");
                for(int j = 0;j < 5;j++){
                    int num = Integer.parseInt(""+row.charAt(j));
                    knappingPattern.set(j, i, num);
                }
            }
            return new KnappingRecipe(input, knappingPattern, outputStack, id);
        }
        @Override
        public KnappingRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            KnappingPattern pattern = new KnappingPattern();
            for(int i = 0;i < 25;i++){
                pattern.set(i, buf.readVarInt());
            }
            return new KnappingRecipe(input, pattern, output, id);
        }
        @Override
        public void write(PacketByteBuf buf, KnappingRecipe recipe) {
            recipe.input.write(buf);
            buf.writeItemStack(recipe.outputStack);
            for(int i = 0;i < 25;i++){
                buf.writeVarInt(recipe.pattern.get(i));
            }
        }
    }
    @Override
    public RecipeType<?> getType() {
        return KnappingRecipeType.INSTANCE;
    }
    public static class KnappingRecipeType implements RecipeType<KnappingRecipe>{
        private KnappingRecipeType() {}
        public static final KnappingRecipeType INSTANCE = new KnappingRecipeType();
        public static final Identifier ID = TechCraft.identifier("knapping_recipe");
    }

    @Override
    public ItemStack craft(KnappingPatternInventory inventory) {
        return ItemStack.EMPTY;
    }
    @Override
    public boolean fits(int width, int height) {
        return false;
    }
}
