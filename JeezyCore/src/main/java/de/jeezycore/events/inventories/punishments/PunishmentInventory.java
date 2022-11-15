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

    Inventory punishment_menu;

    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if(e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Punishments")) {
            createPunishmentInventory(e);
            getData(e);
            displayPunishments(e);
        }
    }

    private void displayPunishments(org.bukkit.event.inventory.InventoryClickEvent e) {
        try {
            int d = 1;
            JSONParser jsParser = new JSONParser();
            JSONArray jsonA = (JSONArray) jsParser.parse(LogsSQL.ban_log);
            for (int i = 0; i < jsonA.size(); i++) {

                JSONObject jsonOB = (JSONObject) jsParser.parse(jsonA.get(i).toString());

                ItemStack rank = new ItemStack(Material.PAPER, 1);
                String displayName = "§4#§7"+0+0+d++;
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
                punishment_menu.setItem(i, rank);

            }
            e.getWhoClicked().openInventory(punishment_menu);
            e.setCancelled(true);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void getData(org.bukkit.event.inventory.InventoryClickEvent e) {
        LogsSQL logs = new LogsSQL();
        UUIDChecker udc = new UUIDChecker();
        udc.check(ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
        logs.punishment_log(UUID.fromString(UUIDChecker.uuid));
    }

    private void createPunishmentInventory(org.bukkit.event.inventory.InventoryClickEvent e) {
        punishment_menu = Bukkit.createInventory(null, 27,"§8Punishments: §f§l"+ ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
    }
}