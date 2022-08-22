package de.jeezycore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public void onCLickEvent(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (!e.getCurrentItem().getType().getData().getSimpleName().equals("Wool")) {
            return;
        }
            System.out.println("works");



    }


}
