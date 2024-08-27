package de.jeezycore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static de.jeezycore.utils.ArrayStorage.inBuildingMode;

public class PlaceBlocksEvent implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        if (!inBuildingMode.contains(e.getPlayer().getUniqueId()) && !e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }
}