package de.jeezycore.events;

import de.jeezycore.commands.GrantRank;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.utils.UUIDChecker;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InventoryClickEvent implements Listener {

    Inventory profile_inv;
    Inventory manage_menu;

    @EventHandler
    public void onCLickEvent(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null || e.getClickedInventory() == null) {
            return;
        }

        if (e.getClickedInventory().getName().contains("Grant") && !e.getCurrentItem().getItemMeta().getDisplayName().contains(UUIDChecker.uuidName)) {
            e.setCancelled(true);
            JeezySQL mysql = new JeezySQL();

            String get_rank = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
            mysql.grantPlayer(get_rank);

            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage("You §b§lsuccessfully§f granted §l§7"+ UUIDChecker.uuidName +"§f the §l"+e.getCurrentItem().getItemMeta().getDisplayName()+" §frank.");

        } else if(e.getClickedInventory().getName().contains("Grant") && e.getCurrentItem().getItemMeta().getDisplayName().contains(UUIDChecker.uuidName)) {
            profile_inv = Bukkit.createInventory(null, 27,"§8Profile: " +"§f§l"+UUIDChecker.uuidName);
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
            e.getWhoClicked().openInventory(profile_inv);
            e.setCancelled(true);
        }

        if(e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.setCancelled(true);
            e.getWhoClicked().openInventory(GrantRank.grant_inv);
        } else if (e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Manage")) {
            manage_menu = Bukkit.createInventory(null, 27,"§8Manage Menu");
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
            e.getWhoClicked().openInventory(manage_menu);
            e.setCancelled(true);
        } else if(e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Punishments")) {
            e.setCancelled(true);
        }

        if (e.getClickedInventory().getName().contains("Manage Menu") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.setCancelled(true);
            e.getWhoClicked().openInventory(profile_inv);
        } else if(e.getClickedInventory().getName().contains("Manage Menu") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Rank remove")) {
            Inventory inv = Bukkit.createInventory(null, 27,"§8Remove Rank: §f§l"+UUIDChecker.uuidName);
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
        } else if(e.getClickedInventory().getName().contains("§8Remove Rank: §f§l"+UUIDChecker.uuidName) && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.setCancelled(true);
            e.getWhoClicked().openInventory(manage_menu);
        }

        if (e.getClickedInventory().getName().contains("§8Remove Rank: §f§l"+UUIDChecker.uuidName) && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aYes")) {
            e.setCancelled(true);
            JeezySQL removeRank = new JeezySQL();
            removeRank.removeRankGui((Player) e.getWhoClicked());
            e.getWhoClicked().closeInventory();
        } else if (e.getClickedInventory().getName().contains("§8Remove Rank: §f§l"+UUIDChecker.uuidName) && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4No")) {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
        }










    }

}
