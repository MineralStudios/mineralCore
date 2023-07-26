package de.jeezycore.commands.permissions;

import de.jeezycore.db.RanksSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionRemove implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("permissionRemove") && args.length == 2) {
                if (p.hasPermission("jeezy.core.permission.remove")) {
                    System.out.println(args.length);
                    RanksSQL perms = new RanksSQL();

                    perms.getAllPlayerInformation(p, args[1]);
                    perms.removePerms(args[0], args[1], p);
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
                p.sendMessage("Usage /permissionRemove <perm> <rankName>");
            }
        }
        return true;
    }
}