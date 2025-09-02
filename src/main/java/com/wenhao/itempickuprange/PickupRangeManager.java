package com.wenhao.itempickuprange;

import com.wenhao.itempickuprange.config.ModConfig;
import com.wenhao.itempickuprange.util.PlayerEntityAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PickupRangeManager {

    public static void clampAllPlayers(MinecraftServer server) {
        if (server == null) return;

        PlayerManager playerManager = server.getPlayerManager();
        if (playerManager == null) {
            // Server hasn’t fully started yet, skip safely
            return;
        }

        for (ServerPlayerEntity player : playerManager.getPlayerList()) {
            double oldRange = ((PlayerEntityAccess) player).getCustomPickupRange();
            double newRange = Math.min(
                Math.max(oldRange, ModConfig.get().minPickupRange),
                ModConfig.get().maxPickupRange
            );

            if (newRange != oldRange) {
                ((PlayerEntityAccess) player).setCustomPickupRange(newRange);
                player.sendMessage(Text.of(
                    "[ItemPickUpRange] Your pickup range has been adjusted to " + newRange +
                    " due to updated server limits."
                ), false);
            }
        }
    }
}
