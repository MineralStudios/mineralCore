package de.jeezycore.commands.basic;

import de.jeezycore.db.PlayTimeSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayTime implements CommandExecutor {

    PlayTimeSQL playTimeSQL = new PlayTimeSQL();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("playtime") && args.length == 0) {
                playTimeSQL.showPlayTime(p);
            } else {
                p.sendMessage("Usage: /playtime");
            }
        }
        return true;
    }
}