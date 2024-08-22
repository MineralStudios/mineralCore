package de.jeezycore.commands.basic.msg;

import com.google.common.base.Joiner;
import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.SettingsSQL;
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
    RanksSQL display = new RanksSQL();
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
                settingsSQL.getSettingsData(UUID.fromString(UUIDChecker.uuid));

                if (!settingsSQL.settingsMsg) {
                    p.sendMessage("§9"+result+" §7has turned off his §9private §7messages.");
                    return true;
                }

                if (msg_ignore_array.contains(p.getUniqueId().toString())) {
                    p.sendMessage("§9"+result+" §7has ignored you.");
                    msg_ignore_array.clear();
                    return true;
                }

                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls);

                uc.check(p.getDisplayName());
                display.getColorsForMessages(UUID.fromString(UUIDChecker.uuid));
                String sql = "SELECT * FROM ranks WHERE rankName = '"+display.privateMessageColors+"'";
                display.displayChatRank(sql);

                Bukkit.getPlayer(result).sendMessage("§9From§7 ("+display.rankColor.replace("&", "§")+p.getPlayer().getDisplayName()+"§7)"+"§7 "+input);
                reply_array.put(result, p.getPlayer().getDisplayName());


                uc.check(result);
                display.getColorsForMessages(UUID.fromString(UUIDChecker.uuid));
                sql = "SELECT * FROM ranks WHERE rankName = '"+display.privateMessageColors+"'";
                display.displayChatRank(sql);

                p.sendMessage("§9To§7 ("+display.rankColor.replace("&", "§")+result+"§7)"+"§7 "+input);
                if (settingsSQL.settingsPmSound) {
                    Bukkit.getPlayer(result).playSound(Bukkit.getPlayer(result).getLocation(), Sound.BLAZE_HIT, 1, 1);
                }
            } else {
                p.sendMessage("Usage /r <message>");
            }
        }
        return true;
    }
}