package de.jeezycore.commands.punishments.freeze;

import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Freeze implements CommandExecutor {

    UUIDChecker uuidChecker = new UUIDChecker();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.staff.utils.freeze") || p.hasPermission("jeezy.core.staff.utils.*")) {
                if (cmd.getName().equalsIgnoreCase("freeze") && args.length == 1) {

                    if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                        p.sendMessage("§7You §2successfully §7froze §9"+UUIDChecker.uuidName+"§7.");
                        ArrayStorage.freezeList.add(UUID.fromString(UUIDChecker.uuid));
                    } else {
                        p.sendMessage("§9"+args[0] + " §7isn't §conline §7currently.");
                    }
                } else {
                    p.sendMessage("Usage: /freeze <playerName>");
                }
            } else {
                p.sendMessage("No permission.");
            }
        }
        return true;
    }
}