package de.jeezycore.commands;

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

            if (cmd.getName().equalsIgnoreCase("permissionAdd") && args.length > 0 || args.length == 1) {

                JeezySQL perms = new JeezySQL();
                JeezySQL setPerms = new JeezySQL();

                perms.addPerms(args[0], args[1], p);
                setPerms.setPerms(args[1], p);

                PermissionHandler ph = new PermissionHandler();
                ph.perms(p, args[0]);

            } else {
            p.sendMessage("Usage /permissionAdd <perm> <rankName>");
            }

        }

        return false;
    }
}
