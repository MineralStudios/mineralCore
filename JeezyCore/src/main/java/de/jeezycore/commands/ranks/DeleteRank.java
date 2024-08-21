package de.jeezycore.commands.ranks;

import de.jeezycore.db.RanksSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteRank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        RanksSQL ranksSQL = new RanksSQL();

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.rank.delete")) {
                if (cmd.getName().equalsIgnoreCase("delete-rank") && args.length < 1) {
                    p.sendMessage("Usage: /delete-rank (name)");
                } else {
                    ranksSQL.deleteRank(p, args[0]);
                }
            } else {
                p.sendMessage("No permission.");
            }
        }
        return true;
    }
}
