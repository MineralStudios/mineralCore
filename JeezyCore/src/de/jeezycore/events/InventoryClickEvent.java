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

        System.out.println(JeezySQL.player);
        JeezySQL mysql = new JeezySQL();
        player_name_array.add(JeezySQL.player);

        System.out.println(player_name_array);
        String input = "UPDATE jeezycore " +
                "SET playerName = ? " +
                "WHERE rankName = ?";

        String get_rank = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
        mysql.grantPlayer(input, get_rank);
        player_name_array.remove(JeezySQL.player);

        e.setCancelled(true);
        e.getWhoClicked().closeInventory();
        System.out.println();
        e.getWhoClicked().sendMessage("You §b§lsuccessfully§f granted §l§7"+ UUIDChecker.uuidName +"§f the §l"+e.getCurrentItem().getItemMeta().getDisplayName()+" §frank.");
    }


}
