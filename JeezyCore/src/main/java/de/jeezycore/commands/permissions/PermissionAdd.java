package de.jeezycore.commands.permissions;

import de.jeezycore.db.RanksSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionAdd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.permission.add")) {
            if (cmd.getName().equalsIgnoreCase("permissionAdd") && args.length == 2) {

                    System.out.println(args.length);
                    RanksSQL perms = new RanksSQL();

                    perms.getAllPlayerInformation(p, args[1]);
                    perms.addPerms(args[0], args[1], p);

            } else {
            p.sendMessage("Usage /permissionAdd <perm> <rankName>");
            }
            } else {
                p.sendMessage("No permission.");
            }
        }
        return true;
    }
}