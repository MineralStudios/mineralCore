package de.jeezycore.events;

import de.jeezycore.db.JeezySQL;
import de.jeezycore.db.PlayersSQL;
import de.jeezycore.disguise.manger.DisguiseManager;
import de.jeezycore.utils.FakePlayerChecker;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class QuitEvent implements Listener {
  private final DisguiseManager disguiseManager;
  PlayersSQL playersSQL = new PlayersSQL();
  JeezySQL jeezySQL = new JeezySQL();

  @EventHandler
  private void onQuit(PlayerQuitEvent event) {
    if (FakePlayerChecker.isFakePlayer(event.getPlayer()))
      return;

    playersSQL.lastSeen(event);
    disguiseManager.deleteDisguise(event.getPlayer());
    jeezySQL.rankMonthlyDurationCalculator(event.getPlayer());
  }
}