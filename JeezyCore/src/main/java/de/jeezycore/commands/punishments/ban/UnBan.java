package de.jeezycore.commands.punishments.ban;

import de.jeezycore.db.BanSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnBan implements CommandExecutor {

    BanSQL execute = new BanSQL();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("jeezy.core.punishments.unban")) {
            if (cmd.getName().equalsIgnoreCase("unban") && args.length > 0) {
                    execute.unban(args[0], p.getPlayer());
            } else {
                p.sendMessage("Usage: /unban <player>.");
            }
            } else {
                p.sendMessage("No permission.");
            }
        } else {
            execute.unbanConsole(args[0], sender);
        }
        return true;
    }
}