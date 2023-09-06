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

public class Report implements CommandExecutor {
    BungeeChannelApi bungeeChannelApi = new BungeeChannelApi();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {



        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("report") && args.length >= 2) {

                List<String> ls = new ArrayList<String>(Arrays.asList(args));
                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls).replace(args[0], "").replaceAll("\\s+", " ").trim();

                bungeeChannelApi.reportPlayer(p, args[0], input);
            } else {
                p.sendMessage("Usage: /report <player><message>");
            }
        }
        return true;
    }
}