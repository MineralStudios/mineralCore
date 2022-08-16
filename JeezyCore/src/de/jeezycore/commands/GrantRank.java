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
                Inventory inv = Bukkit.createInventory(null, 18, ChatColor.GRAY + "Grant " +ChatColor.WHITE+ args[0]);
                Wool wool = new Wool(DyeColor.RED);
                ItemStack rank = new ItemStack(wool.toItemStack());
                ItemMeta rankMeta = rank.getItemMeta();
                inv.setItem(0, rank);
                p.openInventory(inv);
            }

        }

        return false;
    }
}
