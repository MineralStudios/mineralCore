package de.jeezycore.commands.staff;

import de.jeezycore.db.StaffSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffRankEnable implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("staffrank-enable") && args.length == 1) {

                StaffSQL execute = new StaffSQL();
                execute.addToStaff(args[0], p);

            } else {
                p.sendMessage("Usage: /staffrank-enable <rankName>");
            }

        }

        return true;
    }
}
