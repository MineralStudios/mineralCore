package de.jeezycore.commands.punishments.ban;

import de.jeezycore.db.BanSQL;
import de.jeezycore.db.JeezySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TempBan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("tempban") && args.length >= 3) {
                BanSQL execute = new BanSQL();

                execute.tempBan(args[0], args[1], args[2], p);

            } else {
                p.sendMessage("Usage: /tempban <player><time><reason>");
            }
        }
        return false;
    }
}