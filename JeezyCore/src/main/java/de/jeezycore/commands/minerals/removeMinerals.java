package de.jeezycore.commands.minerals;

import de.jeezycore.db.MineralsSQL;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class removeMinerals implements CommandExecutor {

    MineralsSQL mineralsSQL = new MineralsSQL();
    UUIDChecker uuidChecker = new UUIDChecker();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.minerals.remove")) {
                if (cmd.getName().equalsIgnoreCase("removeminerals") && args.length == 2) {
                    uuidChecker.check(args[0]);
                    mineralsSQL.removeMinerals(p, UUIDChecker.uuid, Integer.parseInt(args[1]));
                } else {
                    p.sendMessage("Usage: /removeminerals <player> <amount>");
                }
            } else {
                p.sendMessage("No permission!");
            }
        }
        return true;
    }
}