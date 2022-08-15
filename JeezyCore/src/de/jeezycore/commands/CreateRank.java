package de.jeezycore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateRank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

           if (cmd.getName().equalsIgnoreCase("create-rank") && args.length == 0 || args.length == 1) {
              p.sendMessage("Usage: /create-rank (name) (color)");
           } else {
               p.sendMessage("Rank name: " + args[0] + " Color: " + args[1]);
           }
        }
        return false;
    }
}
