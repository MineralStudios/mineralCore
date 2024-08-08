package de.jeezycore.events;

import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.PlayersSQL;
import de.jeezycore.disguise.manger.DisguiseManager;
import de.jeezycore.tablist.VuzleTAB;
import lombok.RequiredArgsConstructor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class QuitEvent implements Listener {
  private final DisguiseManager disguiseManager;
  PlayersSQL playersSQL = new PlayersSQL();
  RanksSQL ranksSQL = new RanksSQL();
  VuzleTAB vuzleTAB = new VuzleTAB();

  @EventHandler
  private void onQuit(PlayerQuitEvent event) {
    playersSQL.lastSeen(event);
    disguiseManager.deleteDisguise(event.getPlayer());
    ranksSQL.rankMonthlyDurationCalculator(event.getPlayer());

    CraftPlayer craftPlayer = (CraftPlayer) event.getPlayer();
    vuzleTAB.removePlayersFromListOnQuit(event, craftPlayer.getHandle());
  }
}