package de.jeezycore.commands.punishments;

import com.google.common.base.Joiner;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ban implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ban") && args.length > 1) {
                BanList ban_list_name = Bukkit.getBanList(BanList.Type.NAME);
                BanList ban_list_ip =  Bukkit.getBanList(BanList.Type.IP);
                List<String> ls = new ArrayList<String>(Arrays.asList(args));
                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls).replace(args[0], "");

                Player target = Bukkit.getServer().getPlayer(args[0]);

                ban_list_name.addBan(target.getName(),input, null, null);
            } else {
                p.sendMessage("Usage: /ban <player><reason>.");
            }

        }


        return false;
    }
}
