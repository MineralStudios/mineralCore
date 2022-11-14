package de.jeezycore.events.inventories.grant;

import de.jeezycore.db.JeezySQL;
import de.jeezycore.discord.messages.grant.RealtimeGrant;
import de.jeezycore.events.InventoryClickEvent;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.PermissionHandler;
import org.bukkit.inventory.Inventory;

public class GrantInventory {

    Inventory profile_inv;
    Inventory manage_menu;

    Inventory punishment_menu;

    public void grantInventory(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getClickedInventory().getName().contains("Grant") && !e.getCurrentItem().getItemMeta().getDisplayName().contains(ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()))) {

            executeMYSQL(e);
            executePermissions(e);

            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage("You §b§lsuccessfully§f granted §l§7" + ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()) + "§f the §l" + e.getCurrentItem().getItemMeta().getDisplayName() + " §frank.");

            executeDiscord(e);
            e.setCancelled(true);
        }
    }

    private void executePermissions(org.bukkit.event.inventory.InventoryClickEvent e) {
        PermissionHandler ph = new PermissionHandler();
        ph.onGranting(e.getWhoClicked());
    }


    private void executeMYSQL(org.bukkit.event.inventory.InventoryClickEvent e) {
        JeezySQL mysql = new JeezySQL();
        String get_rank = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
        mysql.grantPlayer(get_rank, e.getWhoClicked().getUniqueId());
        mysql.onGrantingPerms(e.getWhoClicked());
    }


    private void executeDiscord(org.bukkit.event.inventory.InventoryClickEvent e) {
        RealtimeGrant grant_discord = new RealtimeGrant();
        grant_discord.realtimeChatOnGranting(ArrayStorage.grant_array.get(e.getWhoClicked().getUniqueId()), ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()), e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName().substring(2));
    }

}
