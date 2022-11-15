package de.jeezycore.events.inventories.profiles;

import de.jeezycore.utils.ArrayStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ProfileInventory {
    Inventory profile_inv;

    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if(e.getClickedInventory().getName().contains("Grant") && e.getCurrentItem().getItemMeta().getDisplayName().contains(ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()))) {
            createProfileInventory(e);
            putArray(e);
            createProfileMenuItems();
            e.getWhoClicked().openInventory(ArrayStorage.profile_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        } else if(e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.setCancelled(true);
            e.getWhoClicked().openInventory(ArrayStorage.grant_inv_array.get(e.getWhoClicked().getName()));
        }
    }

    private void createProfileMenuItems() {
        ItemStack back = new ItemStack(Material.REDSTONE);
        ItemStack manage_rank = new ItemStack(Material.REDSTONE_BLOCK);
        ItemStack punishments = new ItemStack(Material.WATCH);
        ItemMeta backm = back.getItemMeta();
        ItemMeta manage_rankm = manage_rank.getItemMeta();
        ItemMeta punishmentsm = punishments.getItemMeta();
        backm.setDisplayName("§cBack");
        manage_rankm.setDisplayName("§3Manage");
        punishmentsm.setDisplayName("§4Punishments");
        back.setItemMeta(backm);
        manage_rank.setItemMeta(manage_rankm);
        punishments.setItemMeta(punishmentsm);
        profile_inv.setItem(9, back);
        profile_inv.setItem(13, manage_rank);
        profile_inv.setItem(17, punishments);
    }

    private void createProfileInventory(org.bukkit.event.inventory.InventoryClickEvent e) {
        profile_inv = Bukkit.createInventory(null, 27,"§8Profile: " +"§f§l"+ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
    }

    private void putArray(org.bukkit.event.inventory.InventoryClickEvent e) {
        ArrayStorage.profile_inv_array.put(e.getWhoClicked().getName(), profile_inv);
    }
}