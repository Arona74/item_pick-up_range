package com.wenhao.itempickuprange;

import com.wenhao.itempickuprange.commands.PickupRangeCommand;
import com.wenhao.itempickuprange.config.ModConfig;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import java.io.File;

public class ItemPickUpRangeMod implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            PickupRangeCommand.register(dispatcher);
        });

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            File configDir = new File("config");
            ModConfig.load(configDir);

            PickupRangeManager.clampAllPlayers(server);
        });
    }
}
