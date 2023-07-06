package de.jeezycore.events.inventories.manage;

import de.jeezycore.db.JeezySQL;
import de.jeezycore.discord.messages.grant.RealtimeGrant;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ManageInventory {

    Inventory manage_menu;

    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Manage")) {
            createManageInventory();
            putArray(e);
            createManageItems();
            e.getWhoClicked().openInventory(ArrayStorage.manage_menu_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        }
        if (e.getClickedInventory().getName().contains("Manage Menu") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.getWhoClicked().openInventory(ArrayStorage.profile_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        } else if(e.getClickedInventory().getName().contains("Manage Menu") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Rank remove")) {
            createManageOptionInventory(e);


        }
        if(e.getClickedInventory().getName().contains("§8Remove Rank: §f§l"+ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId())) && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.getWhoClicked().openInventory(ArrayStorage.manage_menu_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        }

        if (e.getClickedInventory().getName().contains("§8Remove Rank: §f§l"+ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId())) && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aYes")) {
            executeMYSQL(e);
            executePermissions(e);
            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
        } else if (e.getClickedInventory().getName().contains("§8Remove Rank: §f§l"+ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId())) && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4No")) {
            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
        }
    }

    private void createManageOptionInventory(org.bukkit.event.inventory.InventoryClickEvent e) {
        Inventory inv = Bukkit.createInventory(null, 27,"§8Remove Rank: §f§l"+ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
        ItemStack go_back = new ItemStack(Material.REDSTONE);
        ItemMeta go_backm = go_back.getItemMeta();
        go_backm.setDisplayName("§cBack");


        ItemStack remove_yes = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta remove_yesm = remove_yes.getItemMeta();
        remove_yesm.setDisplayName("§aYes");

        ItemStack remove_no = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta remove_nom = remove_no.getItemMeta();
        remove_nom.setDisplayName("§4No");

        go_back.setItemMeta(go_backm);
        remove_yes.setItemMeta(remove_yesm);
        remove_no.setItemMeta(remove_nom);

        inv.setItem(9, go_back);
        inv.setItem(12, remove_yes);
        inv.setItem(14, remove_no);
        e.getWhoClicked().openInventory(inv);
        e.setCancelled(true);
    }
    private void createManageInventory() {
        manage_menu = Bukkit.createInventory(null, 27, "§8Manage Menu");
    }

    private void putArray(org.bukkit.event.inventory.InventoryClickEvent e) {
        ArrayStorage.manage_menu_inv_array.put(e.getWhoClicked().getName(), manage_menu);
    }

    private void createManageItems() {
        ItemStack remove_rank = new ItemStack(Material.EMERALD);
        ItemMeta remove_rankm = remove_rank.getItemMeta();
        remove_rankm.setDisplayName("§4Rank remove");
        remove_rank.setItemMeta(remove_rankm);

        ItemStack go_back = new ItemStack(Material.REDSTONE);
        ItemMeta go_backm = remove_rank.getItemMeta();
        go_backm.setDisplayName("§cBack");
        go_back.setItemMeta(go_backm);

        manage_menu.setItem(9, go_back);
        manage_menu.setItem(13, remove_rank);
    }

    private void executePermissions(org.bukkit.event.inventory.InventoryClickEvent e) {
        PermissionHandler ph = new PermissionHandler();
        ph.onUnGranting(e.getWhoClicked());
    }


    private void executeMYSQL(org.bukkit.event.inventory.InventoryClickEvent e) {
        JeezySQL removeRank = new JeezySQL();
        removeRank.onUnGrantingPerms(e.getWhoClicked());
        removeRank.removeRankGui((Player) e.getWhoClicked());

    }
}