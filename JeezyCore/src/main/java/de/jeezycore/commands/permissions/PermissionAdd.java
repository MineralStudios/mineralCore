package de.jeezycore.commands.permissions;

import de.jeezycore.db.JeezySQL;
import de.jeezycore.utils.PermissionHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionAdd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("permissionAdd") && args.length == 2) {
                if (p.hasPermission("jeezy.core.permission.add")) {
                    System.out.println(args.length);
                    JeezySQL perms = new JeezySQL();

                    perms.addPerms(args[0], args[1], p);
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
            p.sendMessage("Usage /permissionAdd <perm> <rankName>");
            }
        }
        return false;
    }
}