package de.jeezycore.commands.minerals;

import de.jeezycore.db.MineralsSQL;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addMinerals implements CommandExecutor {
    MineralsSQL mineralsSQL = new MineralsSQL();
    UUIDChecker uuidChecker = new UUIDChecker();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.minerals.add")) {
                if (cmd.getName().equalsIgnoreCase("addminerals") && args.length == 2) {
                    uuidChecker.check(args[0]);
                    mineralsSQL.addMinerals(p, UUIDChecker.uuid, Integer.parseInt(args[1]), "&7You &2successfully &7added &9"+args[1]+" &fminerals &7to &9&l"+ UUIDChecker.uuidName+"&7.");
                } else {
                    p.sendMessage("Usage: /addminerals <player> <amount>");
                }
            } else {
                p.sendMessage("No permission!");
            }
        }
        return true;
    }
}