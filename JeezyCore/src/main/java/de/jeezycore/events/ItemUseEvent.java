package de.jeezycore.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemUseEvent implements Listener {

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        try {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cWorldEdit-Helper") || e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cWorldEdit-Helper")) {
                Bukkit.getServer().dispatchCommand(e.getPlayer(), "thru");
            }
            if (e.getAction() == Action.LEFT_CLICK_BLOCK && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cWorldEdit-Helper") || e.getAction() == Action.LEFT_CLICK_AIR && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cWorldEdit-Helper")) {
                Bukkit.getServer().dispatchCommand(e.getPlayer(), "ascend");
            }
        } catch (Exception x) {
        }
    }
}
