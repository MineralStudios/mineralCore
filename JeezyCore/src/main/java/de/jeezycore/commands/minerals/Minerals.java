package de.jeezycore.commands.minerals;

import de.jeezycore.db.MineralsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Minerals implements CommandExecutor {
    MineralsSQL mineralsSQL = new MineralsSQL();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("minerals") || cmd.getName().equalsIgnoreCase("coins"))  {
                mineralsSQL.mineralsBalance(p);
            }
        }
        return true;
    }
}