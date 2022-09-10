package de.jeezycore.commands;

import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Msg implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (Bukkit.getServer().getPlayerExact(args[0]) == null) {
                p.sendMessage("§4This player is not online anymore.");
                return true;
            }

            if (cmd.getName().equalsIgnoreCase("msg") && args.length > 1) {
                List<String> ls = new ArrayList<String>(Arrays.asList(args));

                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls).replace(args[0], "");
                p.sendMessage("§8§l(§4§lmsg§8§l) §2§l"+p.getPlayer().getDisplayName()+"§7: "+input);
                Bukkit.getPlayer(args[0]).sendMessage("§8§l(§4§lmsg§8§l) §2§l"+p.getPlayer().getDisplayName()+"§7: "+input);

            } else {
                p.sendMessage("Usage /msg <player> <message>");
            }

        }


        return false;
    }
}
