package de.jeezycore.commands.staff;

import de.jeezycore.events.inventories.punishments.PunishmentInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Logs implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.staff.logs")) {
                if (cmd.getName().equalsIgnoreCase("logs") && args.length == 1) {
                    PunishmentInventory punishmentInventory = new PunishmentInventory();
                    punishmentInventory.logsMenu(p, args[0]);
                } else {
                    p.sendMessage("Usage: /logs <playerName>");
                }
            } else {
                p.sendMessage("No permission");
            }
        }
        return true;
    }
}