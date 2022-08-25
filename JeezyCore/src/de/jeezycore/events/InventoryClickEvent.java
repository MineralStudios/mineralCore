package de.jeezycore.events;

import de.jeezycore.db.JeezySQL;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

import static de.jeezycore.db.JeezySQL.player;
import static de.jeezycore.db.JeezySQL.player_name_array;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public void onCLickEvent(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (!e.getCurrentItem().getType().getData().getSimpleName().equals("Wool")) {
            return;
        }
        JeezySQL mysql = new JeezySQL();

        String get_rank = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
        mysql.grantPlayer(get_rank);


        e.setCancelled(true);
        e.getWhoClicked().closeInventory();
        e.getWhoClicked().sendMessage("You §b§lsuccessfully§f granted §l§7"+ UUIDChecker.uuidName +"§f the §l"+e.getCurrentItem().getItemMeta().getDisplayName()+" §frank.");
    }


}
