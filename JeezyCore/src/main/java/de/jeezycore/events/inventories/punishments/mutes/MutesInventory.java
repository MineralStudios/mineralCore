package de.jeezycore.events.inventories.punishments.mutes;

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

public class MutesInventory {

    Inventory mute_inventory;

    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if(e.getClickedInventory().getName().contains("Punishments") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§9§lMutes")) {
            displayMutePunishments(e);
        } else if(e.getClickedInventory().getName().contains("Mutes") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.getWhoClicked().openInventory(ArrayStorage.punishments_menu_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        } else if (e.getClickedInventory().getName().contains("Mutes") && e.getCurrentItem().getData().toString().contains("PAPER")) {
            e.setCancelled(true);
        }
    }

    private void displayMutePunishments(org.bukkit.event.inventory.InventoryClickEvent e) {
        try {
            getData(e);
            createMuteInventory(e);

            if (LogsSQL.mute_log != null) {

            int num = 1;

            JSONParser jsParser = new JSONParser();
            JSONArray jsonA = (JSONArray) jsParser.parse(LogsSQL.mute_log);
            for (int i = 0; i < jsonA.size(); i++) {

                JSONObject jsonOB = (JSONObject) jsParser.parse(jsonA.get(i).toString());

                ItemStack rank = new ItemStack(Material.PAPER, 1);

                String displayName = "§9#§7"+0+0+num++;
                ItemMeta rankMeta = rank.getItemMeta();
                List<String> desc = new ArrayList<String>();
                desc.add(0, "§8§m-----------------------------------");
                desc.add(1, "§9UUID: §7"+ UUIDChecker.uuid+"");
                desc.add(2,"§9Reason: §7"+jsonOB.get("reason"));
                desc.add(3,"§9mute_start: §7"+jsonOB.get("mute_start"));
                desc.add(4,"§9mute_end: §7"+jsonOB.get("mute_end"));
                desc.add(5,"§9muted_by: §7"+jsonOB.get("muted by"));
                desc.add(6, "§8§m-----------------------------------");
                rankMeta.setDisplayName(displayName);
                rankMeta.setLore(desc);
                rank.setItemMeta(rankMeta);
                mute_inventory.setItem(i+1, rank);

                }
            }
            back();
            e.getWhoClicked().openInventory(mute_inventory);
            e.setCancelled(true);
            wipeData();

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void createMuteInventory(org.bukkit.event.inventory.InventoryClickEvent e) {
        mute_inventory = Bukkit.createInventory(null, 27,"§8Mutes: §f§l"+ ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
    }

    private void back() {
        ItemStack back = new ItemStack(Material.REDSTONE);
        ItemMeta backm = back.getItemMeta();
        backm.setDisplayName("§cBack");
        back.setItemMeta(backm);
        mute_inventory.setItem(0, back);
    }

    private void wipeData() {
        LogsSQL logsSQL = new LogsSQL();
        logsSQL.refreshData();
    }

    private void getData(org.bukkit.event.inventory.InventoryClickEvent e) {
        LogsSQL logs = new LogsSQL();
        UUIDChecker udc = new UUIDChecker();
        udc.check(ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
        logs.punishment_log(UUID.fromString(UUIDChecker.uuid));
        System.out.println(ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
    }

}
