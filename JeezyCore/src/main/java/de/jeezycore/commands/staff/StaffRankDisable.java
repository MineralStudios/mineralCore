package de.jeezycore.commands.staff;

import de.jeezycore.db.StaffSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffRankDisable implements CommandExecutor {
    StaffSQL execute = new StaffSQL();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.staff.disable")) {
            if (cmd.getName().equalsIgnoreCase("staffrank-disable") && args.length == 1) {

                    execute.removeFromStaff(args[0], p);

            } else {
                p.sendMessage("Usage: /staffrank-disable <rankName>");
            }
            } else {
                p.sendMessage("No permission.");
            }
        }
        return true;
    }
}