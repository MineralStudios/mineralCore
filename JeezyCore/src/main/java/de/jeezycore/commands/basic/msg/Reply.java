package de.jeezycore.commands.basic.msg;

import com.google.common.base.Joiner;
import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.SettingsSQL;
import de.jeezycore.utils.BungeeChannelApi;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static de.jeezycore.utils.ArrayStorage.*;
import static de.jeezycore.utils.ArrayStorage.msg_ignore_array;

public class Reply implements CommandExecutor {

    BungeeChannelApi bungeeChannelApi = new BungeeChannelApi();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            List<String> ls = new ArrayList<String>(Arrays.asList(args));

            if (cmd.getName().equalsIgnoreCase("r") && args.length != 0) {

                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls);

                bungeeChannelApi.BungeeReply(p, input);

            } else {
                p.sendMessage("Usage /r <message>");
            }
        }
        return true;
    }
}