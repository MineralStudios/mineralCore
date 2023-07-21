package de.jeezycore.commands.punishments.wipe;

import de.jeezycore.db.WipeSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WipeBans implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;
            if (p.hasPermission("jeezy.core.punishments.wipe.bans")) {
            if (cmd.getName().equalsIgnoreCase("wipebans") && args.length == 1) {
                    WipeSQL execute = new WipeSQL();
                    execute.wipeBans(args[0], p);
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
                p.sendMessage("Usage: /wipebans <player>");
            }
        }
        return true;
    }
}