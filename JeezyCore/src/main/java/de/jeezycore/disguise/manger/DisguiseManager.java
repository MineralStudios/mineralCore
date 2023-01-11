package de.jeezycore.disguise.manger;

import de.jeezycore.main.Main;
import de.jeezycore.disguise.Disguise;
import de.jeezycore.utils.HTTPUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DisguiseManager {
  private final HTTPUtility httpUtility;
  private final Map<UUID, Disguise> playerIdToDisguise = new HashMap<>();

  public DisguiseManager(Main plugin, HTTPUtility httpUtility) {

    this.httpUtility = httpUtility;

    // Refresh to remove disguises from a previous instance of plugin
    // This is basically /reload support (not recommended)
    Bukkit.getOnlinePlayers().forEach(player -> {
      Bukkit.getOnlinePlayers().forEach(all -> {
        all.hidePlayer(player);
        all.showPlayer(player);
      });
    });
  }

  public void loadDisguiseInfo(String playerName, HTTPUtility.GetTextureResponse response) {
    httpUtility.getTextureAndSignature(playerName, response);
  }

  public void applyDisguise(Player player, String name, String texture, String signature) {
    if (hasDisguise(player)) {
      deleteDisguise(player);
    }

    Disguise disguise = new Disguise(name, texture, signature);
    playerIdToDisguise.put(player.getUniqueId(), disguise);
    disguise.apply(Main.getPlugin(Main.class), player);
  }

  public void deleteDisguise(Player player) {
    if (!hasDisguise(player)) return;
    Disguise existing = getDisguise(player).get();
    existing.remove(Main.getPlugin(Main.class), player);
    playerIdToDisguise.remove(player.getUniqueId());
  }

  public Optional<Disguise> getDisguise(Player player) {
    return Optional.ofNullable(
        playerIdToDisguise.get(player.getUniqueId())
    );
  }

  public boolean hasDisguise(Player player) {
    return playerIdToDisguise.containsKey(player.getUniqueId());
  }
}
