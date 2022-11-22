package de.jeezycore.commands.punishments.wipe;

import de.jeezycore.db.WipeSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WipeAll implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("wipeall") && args.length == 0) {

                if (p.hasPermission("jeezy.core.punishments.wipe.all")) {
                    WipeSQL execute = new WipeSQL();
                    execute.wipeAll(p);
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
                p.sendMessage("Usage: /wipeall");
            }
        }
        return true;
    }
}