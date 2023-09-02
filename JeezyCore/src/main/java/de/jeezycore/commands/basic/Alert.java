package de.jeezycore.commands.basic;

import com.google.common.base.Joiner;
import de.jeezycore.utils.BungeeChannelApi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Alert implements CommandExecutor {

    BungeeChannelApi bungeeChannelApi = new BungeeChannelApi();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            List<String> ls = new ArrayList<String>(Arrays.asList(args));

            if (cmd.getName().equalsIgnoreCase("alert") && args.length > 0) {
                if (p.hasPermission("jeezy.core.staff.alerts")) {

                    String input = Joiner.on(" ")
                            .skipNulls()
                            .join(ls);
                    bungeeChannelApi.broadcastMessage("§7§l[§4§lALERT§7§l] §f"+input.replace("&", "§"));
                }
                } else {
                p.sendMessage("Usage: /alert <message>");
            }
        }
        return true;
    }
}