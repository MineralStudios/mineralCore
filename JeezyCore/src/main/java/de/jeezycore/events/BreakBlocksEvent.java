package de.jeezycore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import static de.jeezycore.utils.ArrayStorage.inBuildingMode;

public class BreakBlocksEvent implements Listener {

    @EventHandler
    public void onBreakingBlocks(BlockBreakEvent e) {
        if (!inBuildingMode.contains(e.getPlayer().getUniqueId()) && !e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }
}
