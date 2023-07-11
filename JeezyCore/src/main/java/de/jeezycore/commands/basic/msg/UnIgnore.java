package de.jeezycore.commands.basic.msg;

import de.jeezycore.db.SettingsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class UnIgnore implements CommandExecutor {
    SettingsSQL settingsSQL = new SettingsSQL();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("unignore") && args.length == 1) {
                  settingsSQL.removeIgnore(p, args[0]);
                    } else {
                       p.sendMessage("Usage: /unignore <playerName>");
                    }
        }
        return true;
    }
}