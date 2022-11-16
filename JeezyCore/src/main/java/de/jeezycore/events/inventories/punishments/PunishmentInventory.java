package de.jeezycore.events.inventories.punishments;

import de.jeezycore.db.LogsSQL;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishmentInventory {

    Inventory punishment_inventory;
    Inventory ban_inventory;

    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if(e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Punishments")) {
            createPunishmentInventory(e);
            getData(e);
            createPunishmentsMenuItems(e);
            putArray(e);
        }else if(e.getClickedInventory().getName().contains("Punishments") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.getWhoClicked().openInventory(ArrayStorage.profile_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        } else if(e.getClickedInventory().getName().contains("Punishments") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4§lBans")) {
            displayBanPunishments(e);
        }  else if(e.getClickedInventory().getName().contains("Punishments") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4§lBans")) {
            e.setCancelled(true);
        }

        if(e.getClickedInventory().getName().contains("Punishments") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.getWhoClicked().openInventory(ArrayStorage.profile_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        } else if(e.getClickedInventory().getName().contains("Bans") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.getWhoClicked().openInventory(ArrayStorage.punishments_menu_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        }


        if (e.getClickedInventory().getName().contains("Bans") && e.getCurrentItem().getData().toString().contains("PAPER")) {
            e.setCancelled(true);
        }

    }

    private void displayBanPunishments(org.bukkit.event.inventory.InventoryClickEvent e) {
        try {
            createBanInventory(e);
            int num = 1;
            JSONParser jsParser = new JSONParser();
            JSONArray jsonA = (JSONArray) jsParser.parse(LogsSQL.ban_log);
            for (int i = 0; i < jsonA.size(); i++) {

                JSONObject jsonOB = (JSONObject) jsParser.parse(jsonA.get(i).toString());

                ItemStack rank = new ItemStack(Material.PAPER, 1);
                ItemStack back = new ItemStack(Material.REDSTONE);
                ItemMeta backm = back.getItemMeta();
                backm.setDisplayName("§cBack");
                back.setItemMeta(backm);
                String displayName = "§4#§7"+0+0+num++;
                ItemMeta rankMeta = rank.getItemMeta();
                List<String> desc = new ArrayList<String>();
                desc.add(0, "§8§m-----------------------------------");
                desc.add(1, "§4UUID: §7"+UUIDChecker.uuid+"");
                desc.add(2,"§4Reason: §7"+jsonOB.get("reason"));
                desc.add(3,"§4ban_start: §7"+jsonOB.get("ban_start"));
                desc.add(4,"§4ban_end: §7"+jsonOB.get("ban_end"));
                desc.add(5,"§4banned_by: §7"+jsonOB.get("banned by"));
                desc.add(6, "§8§m-----------------------------------");
                rankMeta.setDisplayName(displayName);
                rankMeta.setLore(desc);
                rank.setItemMeta(rankMeta);
                ban_inventory.setItem(0, back);
                ban_inventory.setItem(i+1, rank);

            }
            e.getWhoClicked().openInventory(ban_inventory);
            e.setCancelled(true);
        } catch (Exception err) {
            err.printStackTrace();
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
        mutesm.setDisplayName("§1§lMutes");
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

    private void createBanInventory(org.bukkit.event.inventory.InventoryClickEvent e) {
        ban_inventory = Bukkit.createInventory(null, 27,"§8Bans: §f§l"+ ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
    }

    private void createPunishmentInventory(org.bukkit.event.inventory.InventoryClickEvent e) {
        punishment_inventory = Bukkit.createInventory(null, 27,"§8Punishments: §f§l"+ ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
    }

    private void putArray(org.bukkit.event.inventory.InventoryClickEvent e) {
        ArrayStorage.punishments_menu_inv_array.put(e.getWhoClicked().getName(), punishment_inventory);
    }

}