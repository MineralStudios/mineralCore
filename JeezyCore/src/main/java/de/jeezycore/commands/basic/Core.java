package de.jeezycore.commands.basic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Core implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
           Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("core")) {
                p.sendMessage("§cJeezyCore §7coded by §9JeezyDevelopment §7with §4§l❤§7.\n§7- Version: §3§l1.0.0§7.");
            }
        }
        return false;
    }
}