package de.jeezycore.events;

import de.jeezycore.main.Main;
import de.jeezycore.tablist.VuzleTAB;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import static de.jeezycore.utils.ArrayStorage.playerRankNames;
import static de.jeezycore.utils.NameTag.scoreboard;

public class PlayerTeleportEvent implements Listener {

    VuzleTAB vuzleTAB = new VuzleTAB();

    @EventHandler
    public void onTeleport(org.bukkit.event.player.PlayerTeleportEvent e) {
        if (e.getCause() == org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }

        if (playerRankNames.contains(e.getPlayer().getDisplayName())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    vuzleTAB.removePlayersFromList();
                    vuzleTAB.addPlayersBackToList(e.getPlayer());
                }
            }.runTaskLater(Main.getPlugin(Main.class), 6L);
        }
    }
}