package de.jeezycore.events;

import de.jeezycore.disguise.manger.DisguiseManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class QuitEvent implements Listener {
  private final DisguiseManager disguiseManager;

  @EventHandler
  private void onQuit(PlayerQuitEvent event) {
    disguiseManager.deleteDisguise(event.getPlayer());
  }
}
