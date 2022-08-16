package de.jeezycore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JeezyCoreGuide implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.sendMessage("§8§l§m-----------------------------------\n");
            player.sendMessage("§b§lJeezy§9§lCore Help\n");
            player.sendMessage("\n§f§lcreate-rank: §9§l<name> <color>\n" +
                    "§9Creates rank.\n" +
                    "§f§lgrant-rank: §9§l<player>\n" +
                    "§9Grants player a rank.\n");
            player.sendMessage("§8§l§m-----------------------------------");


        }

        return true;
    }
}
