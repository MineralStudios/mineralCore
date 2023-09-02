package de.jeezycore.events;

import de.jeezycore.utils.ArrayStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        if (ArrayStorage.freezeList.contains(e.getPlayer().getUniqueId())) {
            e.getPlayer().sendMessage("§4§lYou have been frozen.");
            e.getPlayer().closeInventory();
            e.setCancelled(true);
        }
    }
}