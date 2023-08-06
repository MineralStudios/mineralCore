package de.jeezycore.commands.basic;

import de.jeezycore.utils.BungeeChannelApi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Send implements CommandExecutor {

    BungeeChannelApi bungeeChannelApi = new BungeeChannelApi();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.network.send")) {

                if (cmd.getName().equalsIgnoreCase("send") && args.length == 2) {
                    bungeeChannelApi.sendPlayerToServer(p, args[0], args[1]);
                } else {
                    p.sendMessage("Usage: /send <player> <serverName>");
                }
            } else {
                p.sendMessage("No permission.");
            }
        }
        return true;
    }
}
