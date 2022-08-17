package de.jeezycore.commands;

import de.jeezycore.db.JeezySQL;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;

public class GrantRank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("grant-rank") && args.length == 0) {
                p.sendMessage("Usage /grant-rank (player)");
            } else {
                p.sendMessage("You want to grant a rank to the player: "+args[0]);
                Inventory inv = Bukkit.createInventory(null, 27, ChatColor.AQUA + "Grant " +ChatColor.WHITE+ " §8§l[ "+ChatColor.WHITE+args[0]+" §8§l]");

                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                skullMeta.setDisplayName("§b"+args[0]);
                skullMeta.setOwner(args[0]);

                skull.setItemMeta(skullMeta);
                inv.setItem(4, skull);
                JeezySQL display = new JeezySQL();
                display.displayData();

                System.out.println(display.rankData);
                if (display.rankData.size() == 0) {
                    p.openInventory(inv);
                    return true;
                } else {
                    for (String i : display.rankData) {
                        for (int f = 0; f < display.rankData.size(); f++) {
                            Wool wool = new Wool(DyeColor.RED);
                            ItemStack rank = new ItemStack(wool.toItemStack(1));
                            ItemMeta rankMeta = rank.getItemMeta();
                            rankMeta.setDisplayName(i);
                            rank.setItemMeta(rankMeta);
                            inv.setItem(f, rank);
                        }
                    }
                    p.openInventory(inv);
                }

            }

        }

        return false;
    }
}
