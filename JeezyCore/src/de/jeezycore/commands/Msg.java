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

import static de.jeezycore.utils.MsgArray.reply_array;

public class Msg implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            List<String> ls = new ArrayList<String>(Arrays.asList(args));

            if (cmd.getName().equalsIgnoreCase("msg") && args.length >= 2) {
                if (Bukkit.getServer().getPlayerExact(args[0]) == null) {
                    p.sendMessage("§4This player is not online anymore.");
                    return true;
                }
                if (p.getPlayer().getDisplayName().equals(args[0])) {
                    p.sendMessage("§4You can't message yourself.");
                    return true;
                }


                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls).replace(args[0], "");
                p.sendMessage("§8§l(§4§lmsg§8§l) §2§l"+p.getPlayer().getDisplayName()+"§7: "+input);
                Bukkit.getPlayer(args[0]).sendMessage("§8§l(§4§lmsg§8§l) §2§l"+p.getPlayer().getDisplayName()+"§7: "+input);
                reply_array.remove(p.getPlayer().getDisplayName());
                reply_array.put(args[0], p.getPlayer().getDisplayName());
                System.out.println(reply_array);
            } else if (cmd.getName().equalsIgnoreCase("msg") && args.length < 2) {
                p.sendMessage("Usage /msg <player> <message>");
                return true;
            }


            if (cmd.getName().equalsIgnoreCase("r") && args.length != 0) {
            String result = reply_array.get(p.getPlayer().getDisplayName());
            System.out.println(result);
            if (result == null || Bukkit.getServer().getPlayerExact(result) == null) {
                p.sendMessage("§cThere is nobody to reply to.");
                return true;
            }

                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls);
                Bukkit.getPlayer(result).sendMessage("§8§l(§4§lmsg§8§l) §2§l"+p.getPlayer().getDisplayName()+"§7: "+input);
                reply_array.put(result, p.getPlayer().getDisplayName());
                p.sendMessage("§8§l(§4§lreplied§8§l) §2§l"+p.getPlayer().getDisplayName()+"§7: "+input);

            } else if (cmd.getName().equalsIgnoreCase("r") && args.length == 0) {
                p.sendMessage("Usage /r <message>");
            }


        }


        return false;
    }
}
