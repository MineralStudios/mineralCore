package de.jeezycore.commands.punishments;

import de.jeezycore.db.BanSQL;
import de.jeezycore.db.JeezySQL;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnBan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("unban") && args.length > 0) {

                BanSQL execute = new BanSQL();
                execute.unban(args[0], p.getPlayer());

            } else {
                p.sendMessage("Usage: /unban <player>.");
            }
        }

        return false;
    }
}
