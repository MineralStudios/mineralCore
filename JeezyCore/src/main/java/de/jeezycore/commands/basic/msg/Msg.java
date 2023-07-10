package de.jeezycore.commands.basic.msg;

import com.google.common.base.Joiner;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.db.SettingsSQL;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static de.jeezycore.utils.ArrayStorage.*;

public class Msg implements CommandExecutor {
    JeezySQL display = new JeezySQL();
    SettingsSQL settingsSQL = new SettingsSQL();

    UUIDChecker uc = new UUIDChecker();
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

                uc.check(args[0]);
                settingsSQL.getSettingsData(UUID.fromString(UUIDChecker.uuid));

                if (settingsSQL.settingsMsg) {
                    p.sendMessage("§9"+args[0]+" §7has turned off his §9private §7messages.");
                    return true;
                }

                if (msg_ignore_list.get(Bukkit.getPlayer(args[0]).getUniqueId()) != null) {
                    if (msg_ignore_list.get(Bukkit.getPlayer(args[0]).getUniqueId()).contains(p.getPlayer().getUniqueId().toString())) {
                        p.sendMessage("§9"+args[0]+" §7has ignored you.");
                        return true;
                    }
                }

            String input = Joiner.on(" ")
                    .skipNulls()
                    .join(ls).replace(args[0], "").replaceAll("\\s+", " ").trim();

            uc.check(args[0]);
            display.getColorsForMessages(UUID.fromString(UUIDChecker.uuid));
            String sql = "SELECT * FROM ranks WHERE rankName = '"+display.privateMessageColors+"'";
            display.displayChatRank(sql);
            p.sendMessage("§9To§7 ("+display.rankColor.replace("&", "§")+args[0]+"§7)"+"§7 "+input);

                display.rankColor = null;
                uc.check(p.getDisplayName());
                display.getColorsForMessages(UUID.fromString(UUIDChecker.uuid));
                sql = "SELECT * FROM ranks WHERE rankName = '"+display.privateMessageColors+"'";
                display.displayChatRank(sql);

            Bukkit.getPlayer(args[0]).sendMessage("§9From§7 ("+display.rankColor.replace("&", "§")+p.getPlayer().getDisplayName()+"§7)"+"§7 "+input);
            reply_array.remove(p.getPlayer().getDisplayName());
            reply_array.put(args[0], p.getPlayer().getDisplayName());
            System.out.println(reply_array);


            } else {
                p.sendMessage("Usage /msg <player> <message>");
                return true;
            }
        }
        return true;
    }
}