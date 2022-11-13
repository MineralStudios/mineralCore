package de.jeezycore.events;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.db.LogsSQL;
import de.jeezycore.discord.messages.grant.RealtimeGrant;
import de.jeezycore.discord.messages.realtime.RealtimeChat;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.PermissionHandler;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sun.rmi.runtime.Log;

import java.util.*;


public class InventoryClickEvent implements Listener {

    Inventory profile_inv;
    Inventory manage_menu;

    Inventory punishment_menu;

    @EventHandler
    public void onCLickEvent(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null || e.getClickedInventory() == null) {
            return;
        }

        if (e.getClickedInventory().getName().contains("Grant") && !e.getCurrentItem().getItemMeta().getDisplayName().contains(ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()))) {
            e.setCancelled(true);
            JeezySQL mysql = new JeezySQL();
            PermissionHandler ph = new PermissionHandler();
            RealtimeGrant grant_discord = new RealtimeGrant();

            String get_rank = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

            mysql.grantPlayer(get_rank, e.getWhoClicked().getUniqueId());
            mysql.onGrantingPerms(e.getWhoClicked());
            ph.onGranting(e.getWhoClicked());


            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage("You §b§lsuccessfully§f granted §l§7"+ ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()) +"§f the §l"+e.getCurrentItem().getItemMeta().getDisplayName()+" §frank.");

            grant_discord.realtimeChatOnGranting(ArrayStorage.grant_array.get(e.getWhoClicked().getUniqueId()), ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()), e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName().substring(2));
        } else if(e.getClickedInventory().getName().contains("Grant") && e.getCurrentItem().getItemMeta().getDisplayName().contains(ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()))) {
            profile_inv = Bukkit.createInventory(null, 27,"§8Profile: " +"§f§l"+ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
            ArrayStorage.profile_inv_array.put(e.getWhoClicked().getName(), profile_inv);
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
            e.getWhoClicked().openInventory(ArrayStorage.profile_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        }

        if(e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.setCancelled(true);
            e.getWhoClicked().openInventory(ArrayStorage.grant_inv_array.get(e.getWhoClicked().getName()));
        } else if (e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Manage")) {
            manage_menu = Bukkit.createInventory(null, 27,"§8Manage Menu");
            ArrayStorage.manage_menu_inv_array.put(e.getWhoClicked().getName(), manage_menu);
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
            e.getWhoClicked().openInventory(ArrayStorage.manage_menu_inv_array.get(e.getWhoClicked().getName()));
            e.setCancelled(true);
        } else if(e.getClickedInventory().getName().contains("Profile") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Punishments")) {
            punishment_menu = Bukkit.createInventory(null, 27,"§8Punishments: §f§l"+ ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));

            LogsSQL logs = new LogsSQL();
            UUIDChecker udc = new UUIDChecker();
            udc.check(ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()));
            logs.punishment_log(UUID.fromString(UUIDChecker.uuid));

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

        if (e.getClickedInventory().getName().contains("Manage Menu") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.setCancelled(true);
            e.getWhoClicked().openInventory(ArrayStorage.profile_inv_array.get(e.getWhoClicked().getName()));
        } else if(e.getClickedInventory().getName().contains("Manage Menu") && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4Rank remove")) {
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
        } else if(e.getClickedInventory().getName().contains("§8Remove Rank: §f§l"+ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId())) && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cBack")) {
            e.setCancelled(true);
            e.getWhoClicked().openInventory(ArrayStorage.manage_menu_inv_array.get(e.getWhoClicked().getName()));
        }

        if (e.getClickedInventory().getName().contains("§8Remove Rank: §f§l"+ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId())) && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aYes")) {
            JeezySQL removeRank = new JeezySQL();
            PermissionHandler ph = new PermissionHandler();
            removeRank.onUnGrantingPerms(e.getWhoClicked());
            ph.onUnGranting(e.getWhoClicked());
            removeRank.removeRankGui((Player) e.getWhoClicked());
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
        } else if (e.getClickedInventory().getName().contains("§8Remove Rank: §f§l"+ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId())) && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§4No")) {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
        }










    }

}
