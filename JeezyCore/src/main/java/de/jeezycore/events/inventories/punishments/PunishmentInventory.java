package de.jeezycore.events.inventories.punishments;

import de.jeezycore.db.LogsSQL;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.UUID;

public class PunishmentInventory {

    Inventory punishment_inventory;

    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if(e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Punishments")) {
            createPunishmentInventory(e);
            getData(e);
            createPunishmentsMenuItems(e);
            putArray(e);
        }else if(e.getClickedInventory().getName().contains("Punishments") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.getWhoClicked().openInventory(ArrayStorage.profile_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        }
        if(e.getClickedInventory().getName().contains("Punishments") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.getWhoClicked().openInventory(ArrayStorage.profile_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        }
    }

    private void createPunishmentsMenuItems(org.bukkit.event.inventory.InventoryClickEvent e) {
        ItemStack back = new ItemStack(Material.REDSTONE);
        ItemStack bans = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemStack mutes = new ItemStack(Material.WOOL, 1, (short) 11);
        ItemMeta backm = back.getItemMeta();
        ItemMeta bansm = bans.getItemMeta();
        ItemMeta mutesm = mutes.getItemMeta();
        backm.setDisplayName("§cBack");
        bansm.setDisplayName("§4§lBans");
        mutesm.setDisplayName("§9§lMutes");
        back.setItemMeta(backm);
        bans.setItemMeta(bansm);
        mutes.setItemMeta(mutesm);
        punishment_inventory.setItem(9, back);
        punishment_inventory.setItem(12, bans);
        punishment_inventory.setItem(14, mutes);
        e.getWhoClicked().openInventory(punishment_inventory);
        e.setCancelled(true);
    }

    private void getData(org.bukkit.event.inventory.InventoryClickEvent e) {
        LogsSQL logs = new LogsSQL();
        UUIDChecker udc = new UUIDChecker();
        udc.check(ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
        logs.punishment_log(UUID.fromString(UUIDChecker.uuid));
    }

    private void createPunishmentInventory(org.bukkit.event.inventory.InventoryClickEvent e) {
        punishment_inventory = Bukkit.createInventory(null, 27,"§8Punishments: §f§l"+ ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
    }

    private void putArray(org.bukkit.event.inventory.InventoryClickEvent e) {
        ArrayStorage.punishments_menu_inv_array.put(e.getWhoClicked().getName(), punishment_inventory);
    }
}