package de.jeezycore.commands.basic;

import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Broadcast implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            List<String> ls = new ArrayList<String>(Arrays.asList(args));

            if (cmd.getName().equalsIgnoreCase("broadcast") && args.length > 0) {
                if (p.hasPermission("jeezy.core.staff.brodcast")) {

                    String input = Joiner.on(" ")
                            .skipNulls()
                            .join(ls);

                    for (Player ps : Bukkit.getOnlinePlayers()) {
                        ps.sendMessage("§7§l[§6§lBroadcast§7§l] §f"+input.replace("&", "§"));
                    }
                }
                } else {
                p.sendMessage("Usage: /broadcast <message>");
            }
        }
        return true;
    }
}