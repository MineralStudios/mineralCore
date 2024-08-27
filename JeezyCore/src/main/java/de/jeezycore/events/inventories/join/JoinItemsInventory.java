package de.jeezycore.events.inventories.join;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JoinItemsInventory {

    public void setItemsOnJoin(PlayerJoinEvent e) {
        createSwordData(e);
        createAxeData(e);
    }


    private void createSwordData(PlayerJoinEvent e) {
        ItemStack woodSword = new ItemStack(Material.WOOD_SWORD);
        ItemMeta woodSwordMeta = woodSword.getItemMeta();
        woodSwordMeta.setDisplayName("§cWorldEdit-Helper");
        woodSword.setItemMeta(woodSwordMeta);
        e.getPlayer().getInventory().setItem(1, woodSword);
    }


    private void createAxeData(PlayerJoinEvent e) {
        ItemStack woodAxe = new ItemStack(Material.WOOD_AXE);
        ItemMeta woodAxeMeta = woodAxe.getItemMeta();
        woodAxeMeta.setDisplayName("§cWorldEdit");
        woodAxe.setItemMeta(woodAxeMeta);
        e.getPlayer().getInventory().setItem(0, woodAxe);
    }
}