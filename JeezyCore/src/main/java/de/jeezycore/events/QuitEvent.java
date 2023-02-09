package de.jeezycore.events;

import de.jeezycore.db.StatusSQL;
import de.jeezycore.disguise.manger.DisguiseManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class QuitEvent implements Listener {
  private final DisguiseManager disguiseManager;
  StatusSQL statusSQL = new StatusSQL();

  @EventHandler
  private void onQuit(PlayerQuitEvent event) {
    statusSQL.lastSeen(event);
    disguiseManager.deleteDisguise(event.getPlayer());
  }
}