package de.jeezycore.commands.basic.msg;

import com.google.common.base.Joiner;
import de.jeezycore.utils.BungeeChannelApi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Msg implements CommandExecutor {

    BungeeChannelApi bungeeChannelApi = new BungeeChannelApi();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            List<String> ls = new ArrayList<String>(Arrays.asList(args));

            if (cmd.getName().equalsIgnoreCase("msg") && args.length >= 2) {

                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls).replace(args[0], "").replaceAll("\\s+", " ").trim();

                bungeeChannelApi.bungeeMsg(p, args[0], input);

            } else {
                p.sendMessage("Usage /msg <player> <message>");
                return true;
            }
        }
        return true;
    }
}