package com.github.industrialcraft.techcraft;

import com.github.industrialcraft.techcraft.knapping.KnappingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class TCRecipes {
    public static void onInitialize(){
        Registry.register(Registries.RECIPE_SERIALIZER, KnappingRecipe.KnappingRecipeSerializer.ID,
                KnappingRecipe.KnappingRecipeSerializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, KnappingRecipe.KnappingRecipeType.ID, KnappingRecipe.KnappingRecipeType.INSTANCE);
    }
}
