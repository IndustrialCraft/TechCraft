package com.github.industrialcraft.techcraft;

import com.github.industrialcraft.techcraft.knapping.KnappingScreen;
import com.github.industrialcraft.techcraft.knapping.KnappingScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TCScreens {
    public static final ScreenHandlerType<KnappingScreenHandler> KNAPPING_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(TechCraft.identifier("knapping_screen_handler"), KnappingScreenHandler::new);
    public static void onClientInitialize(){
        HandledScreens.register(KNAPPING_SCREEN_HANDLER, KnappingScreen::new);
        KnappingScreen.registerClientRenderingData(Items.FLINT, new Identifier("techcraft", "textures/gui/knapping/stone_knapping_bit.png"), new Identifier("techcraft", "textures/gui/knapping/stone_knapping_bit.png"));
    }
    public static void addPlayerInventory(Consumer<Slot> slotAdder, PlayerInventory playerInventory){
        int m;
        int l;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                slotAdder.accept(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        for (m = 0; m < 9; ++m) {
            slotAdder.accept(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }
}
