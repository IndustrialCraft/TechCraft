package com.github.industrialcraft.techcraft;

import com.github.industrialcraft.techcraft.cables.CableConnectorBlockEntity;
import com.github.industrialcraft.techcraft.knapping.KnappingScreenHandler;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class TechCraft implements ModInitializer {
    @Override
    public void onInitialize() {
        TCRecipes.onInitialize();
        TCBlocks.onInitialize();
        TCItems.onInitialize();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("screen").executes(context -> {
                context.getSource().getPlayer().openHandledScreen(new NamedScreenHandlerFactory(){
                    @Override
                    public Text getDisplayName() {
                        return Text.literal("knapping screen");
                    }
                    @Nullable
                    @Override
                    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                        return new KnappingScreenHandler(syncId, inv, 1, new ItemStack(Items.FLINT, 2));
                    }
                });
                return 1;
            }));
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("connect").then(RequiredArgumentBuilder.<ServerCommandSource,PosArgument>argument("first", BlockPosArgumentType.blockPos()).then(RequiredArgumentBuilder.<ServerCommandSource,PosArgument>argument("second", BlockPosArgumentType.blockPos()).executes(context -> {
                BlockPos pos1 = BlockPosArgumentType.getBlockPos(context, "first");
                BlockPos pos2 = BlockPosArgumentType.getBlockPos(context, "second");
                if(context.getSource().getWorld().getBlockEntity(pos1) instanceof CableConnectorBlockEntity cableConnectorBlockEntity){
                    cableConnectorBlockEntity.connect(pos2);
                }
                return 1;
            }))));
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("getNetwork").then(RequiredArgumentBuilder.<ServerCommandSource,PosArgument>argument("pos", BlockPosArgumentType.blockPos()).executes(context -> {
                BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                if(context.getSource().getWorld().getBlockEntity(pos) instanceof CableConnectorBlockEntity cableConnectorBlockEntity){
                    context.getSource().sendMessage(Text.literal(cableConnectorBlockEntity.getPowerNetwork().toString() + ":" +cableConnectorBlockEntity.getPowerNetwork().getAmount()));
                }
                return 1;
            })));
        });
    }
    public static Identifier identifier(String path){
        return new Identifier("techcraft", path);
    }
}
