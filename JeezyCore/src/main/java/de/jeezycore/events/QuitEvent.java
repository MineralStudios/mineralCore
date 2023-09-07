package de.jeezycore.events;

import de.jeezycore.db.*;
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

  PlayTimeSQL playTimeSQL = new PlayTimeSQL();

  RanksSQL ranksSQL = new RanksSQL();

  FriendsSQL friendsSQL = new FriendsSQL();

  MsgSQL msgSQL = new MsgSQL();

  @EventHandler
  private void onQuit(PlayerQuitEvent event) {
    if (FakePlayerChecker.isFakePlayer(event.getPlayer()))
      return;

    friendsSQL.sendFriendOfflineMessage(event);
    playersSQL.lastSeen(event);
    disguiseManager.deleteDisguise(event.getPlayer());
    ranksSQL.rankMonthlyDurationCalculator(event.getPlayer());
    playTimeSQL.playTimeQuit(event);
    msgSQL.quit(event.getPlayer().getUniqueId());
  }
}