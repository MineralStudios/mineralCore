package de.jeezycore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Wool;

public class GrantRank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("grant-rank") && args.length == 0) {
                p.sendMessage("Usage /grant-rank (player)");
            } else {
                p.sendMessage("You want to grant a rank to the player: "+args[0]);
                Inventory inv = Bukkit.createInventory(null, 18, ChatColor.BLACK + "Grant " +ChatColor.WHITE+ args[0]);
                Wool wool = new Wool(DyeColor.RED);
                ItemStack rank = new ItemStack(wool.toItemStack(1));
                ItemMeta rankMeta = rank.getItemMeta();
                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.setOwner(args[0]);
                skull.setItemMeta(meta);
                inv.setItem(4, skull);
                inv.setItem(9, rank);
                p.openInventory(inv);
            }

        }

        return false;
    }
}
