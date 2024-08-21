package de.jeezycore.commands.basic.gamemodes;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Survival implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.gamemode.survival") || p.hasPermission("jeezy.core.gamemode.bypass")) {
                if (cmd.getName().equalsIgnoreCase("gm0") && args.length == 0) {
                    p.setGameMode(GameMode.SURVIVAL);
                } else {
                    p.sendMessage("Usage: /gm0");
                }
            } else {
                p.sendMessage("No permission.");
            }
        }
        return true;
    }
}