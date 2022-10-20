package de.jeezycore.commands.basic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeChanger implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("day")) {
                p.getPlayer().setPlayerTime(6000, false);
                p.sendMessage("Successfully changed time to day.");
            } else if (cmd.getName().equalsIgnoreCase("night")) {
                p.getPlayer().setPlayerTime(16000, false);
                p.sendMessage("Successfully changed time to night.");
            }
        }

        return false;
    }
}
