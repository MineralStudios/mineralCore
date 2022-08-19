package de.jeezycore.commands;

import de.jeezycore.db.JeezySQL;
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
               JeezySQL mySQL = new JeezySQL();
               String input = "INSERT INTO jeezycore " +
                       "(rankName, rankColor) " +
                       "VALUES " +
                       "(?, ?)";
               mySQL.pushData(input, args[0], args[1]);
               p.sendMessage("Rank name: " + args[0] + " Color: " + args[1]);
           }
        }
        return false;
    }
}
