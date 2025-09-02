package com.wenhao.itempickuprange.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.wenhao.itempickuprange.config.ModConfig;
import com.wenhao.itempickuprange.PickupRangeManager;
import com.wenhao.itempickuprange.util.PlayerEntityAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.File;

public class PickupRangeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("PickupRange")
            // /PickupRange set <value>
            .then(CommandManager.literal("set")
                .then(CommandManager.argument("range", DoubleArgumentType.doubleArg())
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        ServerPlayerEntity player = source.getPlayer();
                        if (player == null) return 0;

                        double inputRange = DoubleArgumentType.getDouble(context, "range");
                        ModConfig config = ModConfig.get();

                        // Enforce config limits
                        if (inputRange < config.minPickupRange || inputRange > config.maxPickupRange) {
                            source.sendFeedback(() -> Text.of(
                                "[Error] Pickup range must be between " +
                                config.minPickupRange + " and " + config.maxPickupRange
                            ), false);
                            return 0;
                        }

                        ((PlayerEntityAccess) player).setCustomPickupRange(inputRange);
                        source.sendFeedback(() -> Text.of("Pickup range set to " + inputRange), false);
                        return 1;
                    })
                )
            )
            // /PickupRange get
            .then(CommandManager.literal("get")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayer();
                    if (player == null) return 0;

                    double currentRange = ((PlayerEntityAccess) player).getCustomPickupRange();
                    source.sendFeedback(() -> Text.of("Your current pickup range is " + currentRange), false);
                    return 1;
                })
            )
            // /PickupRange reload
            .then(CommandManager.literal("reload")
                .requires(source -> source.hasPermissionLevel(2)) // OP only
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    File configDir = new File("config"); // same folder as used in ModConfig
                    ModConfig.load(configDir);

                    // Clamp all online players
                    PickupRangeManager.clampAllPlayers(source.getServer());

                    source.sendFeedback(() -> Text.of("[ItemPickUpRange] Config reloaded and player ranges clamped."), true);
                    return 1;
                })
            )
        );
    }
}
