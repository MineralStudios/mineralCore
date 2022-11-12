package de.jeezycore.commands.punishments.mute;

import de.jeezycore.db.BanSQL;
import de.jeezycore.db.MuteSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnMute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("unmute") && args.length > 0) {
                if (p.hasPermission("jeezy.core.punishments.unmute")) {
                    MuteSQL execute = new MuteSQL();
                    execute.unMute(args[0], p.getPlayer());
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
                p.sendMessage("Usage: /unmute <player>.");
            }
        }
        return false;
    }
}