package de.jeezycore.events;

import de.jeezycore.commands.GrantRank;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.utils.UUIDChecker;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InventoryClickEvent implements Listener {

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
            Inventory inv = Bukkit.createInventory(null, 27,"§8Profile: " +"§f§l"+UUIDChecker.uuidName);
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
            inv.setItem(9, back);
            inv.setItem(13, manage_rank);
            inv.setItem(17, punishments);
            e.getWhoClicked().openInventory(inv);
            e.setCancelled(true);
        }

        if(e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.setCancelled(true);
            e.getWhoClicked().openInventory(GrantRank.grant_inv);
        } else if (e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Manage")) {
            e.setCancelled(true);
        } else if(e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Punishments")) {
            e.setCancelled(true);
        }










    }

}
