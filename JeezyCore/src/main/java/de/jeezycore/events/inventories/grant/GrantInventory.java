package de.jeezycore.events.inventories.grant;

import de.jeezycore.db.RanksSQL;
import de.jeezycore.discord.messages.grant.RealtimeGrant;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.PermissionHandler;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GrantInventory {

    Inventory grant_inv;

    public void run(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getClickedInventory().getName().contains("Grant") && !e.getCurrentItem().getItemMeta().getDisplayName().contains(ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()))) {

            executeMYSQL(e);
            executePermissions(e);

            e.getWhoClicked().closeInventory();

            executeDiscord(e);
            e.setCancelled(true);
        }
    }

    public void grant_menu(Player p, String username) {
        grant_inv = Bukkit.createInventory(null, 27,"§8Grant Menu");
        RanksSQL display = new RanksSQL();
        display.displayData();

        ArrayStorage.grant_inv_array.put(p.getPlayer().getDisplayName(), grant_inv);

        UUIDChecker c = new UUIDChecker();
        c.check(username);

        ArrayStorage.grant_array_names.put(p.getUniqueId(), username);
        ArrayStorage.grant_array.put(p.getUniqueId(), UUID.fromString(UUIDChecker.uuid));
        System.out.println(ArrayStorage.grant_array);

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(ArrayStorage.grant_array_names.get(p.getUniqueId()));

        List<String> player_desc = new ArrayList<String>();
        player_desc.add(0, "§8§m-----------------------------------");
        player_desc.add(1, "§7§lClick§7 to manage players §7§lranks §7and §7§lmore");
        player_desc.add(2, "§8§m-----------------------------------");
        skullMeta.setDisplayName("§b"+ArrayStorage.grant_array_names.get(p.getUniqueId()));
        skullMeta.setLore(player_desc);
        skull.setItemMeta(skullMeta);
        grant_inv.setItem(4, skull);


        if (display.rankData.size() == 0) {
            p.openInventory(ArrayStorage.grant_inv_array.get(p.getPlayer().getDisplayName()));
        } else {
            int i = 0;
            for (Map.Entry<String, String> entry : display.rankData.entrySet()) {
                ItemStack rank = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
                String show_color = RanksSQL.rankColorData.get(i).replace("&", "§");
                String displayName = entry.getKey();
                LeatherArmorMeta rankMeta = (LeatherArmorMeta) rank.getItemMeta();

                 String str = entry.getValue();
                 String[] rgbColors = str.split(", ");


                rankMeta.setColor(Color.fromRGB(Integer.parseInt(rgbColors[0]), Integer.parseInt(rgbColors[1]), Integer.parseInt(rgbColors[2])));

                List<String> desc = new ArrayList<String>();
                desc.add(0, "§8§m-----------------------------------");
                desc.add(1, "§7Click to grant §l"+show_color+displayName+"§7 to " + "§b"+username);
                desc.add(2, "§8§m-----------------------------------");
                rankMeta.setDisplayName(show_color+displayName);
                rankMeta.setLore(desc);
                rank.setItemMeta(rankMeta);
                grant_inv.setItem(9+i, rank);
                ++i;
                rgbColors = null;
            }
        }
        p.openInventory(ArrayStorage.grant_inv_array.get(p.getPlayer().getDisplayName()));
    }




    private void executePermissions(org.bukkit.event.inventory.InventoryClickEvent e) {
        PermissionHandler ph = new PermissionHandler();
        ph.onGranting(e.getWhoClicked());
    }


    private void executeMYSQL(org.bukkit.event.inventory.InventoryClickEvent e) {
        RanksSQL mysql = new RanksSQL();
        String get_rank = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
        mysql.grantPlayer(get_rank, UUID.fromString(UUIDChecker.uuid), e.getWhoClicked());
        mysql.onGrantingPerms(e.getWhoClicked(), get_rank);
    }


    private void executeDiscord(org.bukkit.event.inventory.InventoryClickEvent e) {
        RealtimeGrant grant_discord = new RealtimeGrant();
        grant_discord.realtimeChatOnGranting(ArrayStorage.grant_array.get(e.getWhoClicked().getUniqueId()), ArrayStorage.grant_array_names.get(e.getWhoClicked().getUniqueId()), e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName().substring(2));
    }
}