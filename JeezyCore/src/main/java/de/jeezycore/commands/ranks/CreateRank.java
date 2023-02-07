package de.jeezycore.commands.ranks;

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

           if (cmd.getName().equalsIgnoreCase("create-rank") && args.length < 4) {
              p.sendMessage("Usage: /create-rank (name) (rankRGB) (rankColor) (priority)");
           } else {
               if (p.hasPermission("jeezy.core.rank.create")) {
                   JeezySQL mySQL = new JeezySQL();
                   String input = "INSERT INTO jeezycore " +
                           "(rankName, rankRGB, rankColor, rankPriority) " +
                           "VALUES " +
                           "(?, ?, ?, ?)";
                   mySQL.pushData(input, args[0], args[1].replace(",", ", "), args[2], args[3]);
                   p.sendMessage(mySQL.createRankMsg.replace("{rank}", args[0]).replace("{colorID}", args[2].replace("&", "§")));
               } else {
                   p.sendMessage("No permission.");
               }

           }
        }
        return true;
    }
}
