package de.jeezycore.events;

import de.jeezycore.db.PlayTimeSQL;
import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.PlayersSQL;
import de.jeezycore.disguise.manger.DisguiseManager;
import de.jeezycore.tablist.VuzleTAB;
import lombok.RequiredArgsConstructor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.concurrent.CompletableFuture;
import static de.jeezycore.utils.ArrayStorage.tab_name_list_array;

@RequiredArgsConstructor
public class QuitEvent implements Listener {
  private final DisguiseManager disguiseManager;
  PlayersSQL playersSQL = new PlayersSQL();
  PlayTimeSQL playTimeSQL = new PlayTimeSQL();
  RanksSQL ranksSQL = new RanksSQL();
  VuzleTAB vuzleTAB = new VuzleTAB();

  @EventHandler
  private void onQuit(PlayerQuitEvent event) {
    CompletableFuture.runAsync(() -> {
    playersSQL.lastSeen(event);
    disguiseManager.deleteDisguise(event.getPlayer());
    ranksSQL.rankMonthlyDurationCalculator(event.getPlayer());
    tab_name_list_array.remove(event.getPlayer().getUniqueId());
    CraftPlayer craftPlayer = (CraftPlayer) event.getPlayer();
    vuzleTAB.removePlayersFromListOnQuit(event, craftPlayer.getHandle());
    playTimeSQL.playTimeQuit(event);
    });
  }
}