package de.jeezycore.commands;

import de.jeezycore.db.JeezySQL;
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
                System.out.println(args.length);
                JeezySQL perms = new JeezySQL();

                perms.removePerms(args[0], args[1], p);

            } else {
                p.sendMessage("Usage /permissionRemove <perm> <rankName>");
            }
        }
        return false;
    }
}
