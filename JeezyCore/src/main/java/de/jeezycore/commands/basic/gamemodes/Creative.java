package de.jeezycore.commands.basic.gamemodes;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Creative implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.gamemode.creative") || p.hasPermission("jeezy.core.gamemode.bypass")) {
                if (cmd.getName().equalsIgnoreCase("gm1") && args.length == 0) {
                    p.setGameMode(GameMode.CREATIVE);
                } else {
                    p.sendMessage("Usage: /gm1");
                }
            } else {
                p.sendMessage("No permission.");
            }
        }
        return true;
    }
}