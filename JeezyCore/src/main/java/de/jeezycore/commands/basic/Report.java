package de.jeezycore.commands.basic;

import com.google.common.base.Joiner;
import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.events.chat.StaffChat;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Report implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            List<String> ls = new ArrayList<String>(Arrays.asList(args));

            if (cmd.getName().equalsIgnoreCase("report") && args.length >= 2) {

                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls).replace(args[0], "").replaceAll("\\s+", " ").trim();

                UUIDChecker uc = new UUIDChecker();
                uc.check(args[0]);
                JeezySQL display = new JeezySQL();
                StaffChat staffChat = new StaffChat();
                String sql = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ p.getPlayer().getUniqueId().toString() +"%'";
                display.displayChatRank(sql);

                String show_rank_color_reporter = ColorTranslator.colorTranslator.get(display.rankColor);

                display.rankColor_second = 0;

                String sql2 = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ UUIDChecker.uuid +"%'";
                display.displayChatRank(sql2);

                String show_rank_color_reported = ColorTranslator.colorTranslator.get(display.rankColor_second);

                String report_msg = "§7§l[§4Report§7§l] ("+show_rank_color_reporter+p.getDisplayName()+"§6§l -> §7"+show_rank_color_reported+args[0]+"§7§l) §c§lReason§f§l: §7"+input;

                staffChat.reportChat(p, report_msg);

            } else {
                p.sendMessage("Usage: /report <player><message>");
            }
        }

        return true;
    }
}
