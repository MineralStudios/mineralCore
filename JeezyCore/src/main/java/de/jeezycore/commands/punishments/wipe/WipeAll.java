package de.jeezycore.commands.punishments.wipe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WipeAll implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("wipeall") && args.length == 1) {



            } else {
                p.sendMessage("Usage: /wipeall");
            }

        }

        return false;
    }
}
