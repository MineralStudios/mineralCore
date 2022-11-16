package de.jeezycore.events.inventories.punishments.bans;

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

public class BansInventory {
    Inventory ban_inventory;

    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if(e.getClickedInventory().getName().contains("Punishments") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4§lBans")) {
            displayBanPunishments(e);
        } else if(e.getClickedInventory().getName().contains("Bans") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.getWhoClicked().openInventory(ArrayStorage.punishments_menu_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        } else if (e.getClickedInventory().getName().contains("Bans") && e.getCurrentItem().getData().toString().contains("PAPER")) {
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
                desc.add(1, "§4UUID: §7"+ UUIDChecker.uuid+"");
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

    private void createBanInventory(org.bukkit.event.inventory.InventoryClickEvent e) {
        ban_inventory = Bukkit.createInventory(null, 27,"§8Bans: §f§l"+ ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
    }
}