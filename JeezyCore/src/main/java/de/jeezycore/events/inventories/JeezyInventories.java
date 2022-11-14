package de.jeezycore.events.inventories;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JeezyInventories implements Listener {

    @EventHandler
    public void onCLickEvent(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null || e.getClickedInventory() == null) {
            return;
        }
    }

}
