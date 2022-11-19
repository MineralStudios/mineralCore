package de.jeezycore.commands.staff;

import de.jeezycore.db.StaffSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffRankDisable implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("staffrank-disable") && args.length == 1) {

               StaffSQL execute = new StaffSQL();
                execute.removeFromStaff(args[0], p);

            } else {
                p.sendMessage("Usage: /staffrank-disable <rankName>");
            }
        }


        return true;
    }
}
