package de.jeezycore.commands.punishments.mute;

import de.jeezycore.db.BanSQL;
import de.jeezycore.db.MuteSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TempMute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("tempmute") && args.length >= 3) {
                if (p.hasPermission("jeezy.core.punishments.tempmute")) {
                    MuteSQL execute = new MuteSQL();

                    execute.tempMute(args[0], args[1], args[2], p);
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
                p.sendMessage("Usage: /tempmute <player><time><reason>");
            }
        }
        return false;
    }
}