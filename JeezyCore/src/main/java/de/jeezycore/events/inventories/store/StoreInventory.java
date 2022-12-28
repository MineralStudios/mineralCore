package de.jeezycore.events.inventories.store;

import de.jeezycore.db.MineralsSQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class StoreInventory {

    Inventory store_inv;

    MineralsSQL mineralsSQL = new MineralsSQL();

    public void run(InventoryClickEvent e) {
        if (e.getInventory().getTitle().contains("§8§lStore")) {
            e.setCancelled(true);
        }

    }

    public void store_menu(Player p) {
        store_inv = Bukkit.createInventory(null, 54,"§8§lStore");
        for (int b = 0; b < store_inv.getSize(); b++) {
            ItemStack placeholder = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) ((int) 11));
            ItemMeta placeholderMeta = placeholder.getItemMeta();
            placeholderMeta.setDisplayName("§m");
            placeholder.setItemMeta(placeholderMeta);
            store_inv.setItem(b, placeholder);
        }

            ItemStack currentBalance = new ItemStack(Material.NETHER_STAR, 1);
            ItemMeta currentBalanceMeta = currentBalance.getItemMeta();
            currentBalanceMeta.setDisplayName("§9§lBalance§7: §f§l"+mineralsSQL.minerals(p.getUniqueId().toString())+"§9⛁");
            currentBalance.setItemMeta(currentBalanceMeta);
            store_inv.setItem(4, currentBalance);

            ItemStack storeInfo = new ItemStack(Material.BOOK_AND_QUILL, 1);
            ItemMeta storeInfoMeta = storeInfo.getItemMeta();
            List<String> info_desc = new ArrayList<>();
            info_desc.add("§m§7");
            info_desc.add("§7Welcome to our shop §9"+p.getPlayer().getDisplayName()+"§7!");
            info_desc.add("§7Here you can buy things §7just with your §9minerals§7,");
            info_desc.add("§7that you can §9earn §7in a few ways.");
            info_desc.add("§m§7");
            info_desc.add("§7Do daily rewards to get §9minerals §7/§9daily-rewards§7.");
            info_desc.add("§7Also you can earn §9minerals §7by just playing on our server§7.");
            info_desc.add("§m§7");
            info_desc.add("§7§lBest Regards");
            info_desc.add("§9§lMinerals Team");
            storeInfoMeta.setDisplayName("§7§lInformation");
            storeInfoMeta.setLore(info_desc);
            storeInfo.setItemMeta(storeInfoMeta);
            store_inv.setItem(8, storeInfo);

            ItemStack storeTags = new ItemStack(Material.NAME_TAG, 1);
            ItemMeta storeTagsMeta = storeTags.getItemMeta();
            storeTagsMeta.setDisplayName("§7§lTags");
            storeTags.setItemMeta(storeTagsMeta);
            store_inv.setItem(20, storeTags);

            ItemStack storeRanks = new ItemStack(Material.BOOK, 1);
            ItemMeta storeRanksMeta = storeRanks.getItemMeta();
            storeRanksMeta.setDisplayName("§7§lRanks");
            storeRanks.setItemMeta(storeRanksMeta);
            store_inv.setItem(22, storeRanks);

            ItemStack storeKillEffect = new ItemStack(Material.REDSTONE, 1);
            ItemMeta storeKillEffectMeta = storeKillEffect.getItemMeta();
            storeKillEffectMeta.setDisplayName("§7§lKill Effects");
            storeKillEffect.setItemMeta(storeKillEffectMeta);
            store_inv.setItem(24, storeKillEffect);

            ItemStack storeChatColor = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta storeChatColorMeta = storeChatColor.getItemMeta();
            storeChatColorMeta.setDisplayName("§7§lChat Colors");
            storeChatColor.setItemMeta(storeChatColorMeta);
            store_inv.setItem(40, storeChatColor);

        p.openInventory(store_inv);
    }
}