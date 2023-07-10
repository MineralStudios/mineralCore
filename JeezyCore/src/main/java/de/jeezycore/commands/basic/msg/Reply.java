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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static de.jeezycore.utils.ArrayStorage.msg_ignore_list;
import static de.jeezycore.utils.ArrayStorage.reply_array;

public class Reply implements CommandExecutor {
    JeezySQL display = new JeezySQL();
    SettingsSQL settingsSQL = new SettingsSQL();

    UUIDChecker uc = new UUIDChecker();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            List<String> ls = new ArrayList<String>(Arrays.asList(args));

            if (cmd.getName().equalsIgnoreCase("r") && args.length != 0) {
                String result = reply_array.get(p.getPlayer().getDisplayName());
                System.out.println(result);
                if (result == null || Bukkit.getServer().getPlayerExact(result) == null) {
                    p.sendMessage("§cThere is nobody to reply to.");
                    return true;
                }

                uc.check(result);
                settingsSQL.getSettingsData(p, UUID.fromString(UUIDChecker.uuid));

                if (settingsSQL.settingsMsg) {
                    p.sendMessage("§9"+result+" §7has turned off his §9private §7messages.");
                    return true;
                }

                if (msg_ignore_list.get(Bukkit.getPlayer(result).getUniqueId()) != null) {
                    if (msg_ignore_list.get(Bukkit.getPlayer(result).getUniqueId()).contains(p.getPlayer().getUniqueId().toString())) {
                        p.sendMessage("§9"+result+" §7has ignored you.");
                        return true;
                    }
                }

                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls);

                display.getPlayerInformation(p.getPlayer());
                String sql = "SELECT * FROM ranks WHERE rankName = '"+display.rankNameInformation+"'";
                display.displayChatRank(sql);

                Bukkit.getPlayer(result).sendMessage("§8§l(§4§lmsg§8§l) "+display.rankColor.replace("&", "§")+p.getPlayer().getDisplayName()+"§7: "+input);
                reply_array.put(result, p.getPlayer().getDisplayName());
                p.sendMessage("§8§l(§4§lreplied§8§l) "+display.rankColor.replace("&", "§")+p.getPlayer().getDisplayName()+"§7: "+input);

            } else if (cmd.getName().equalsIgnoreCase("r") && args.length == 0) {
                p.sendMessage("Usage /r <message>");
            }
        }
        return true;
    }
}