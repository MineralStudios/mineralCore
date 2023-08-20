package de.jeezycore.commands;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.BungeeChannelApi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

public class ShortenedHubCmd implements CommandExecutor {

    MemorySection hubConfig = (MemorySection) JeezyConfig.config_defaults.get("hub");

    BungeeChannelApi bungeeChannelApi = new BungeeChannelApi();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("l") && args.length == 0) {
                if (hubConfig.getBoolean("enabled")) {
                    bungeeChannelApi.sendToHub(p);
                }
            } else {
                p.sendMessage("Usage: /l");
            }
        }
        return true;
    }
}
