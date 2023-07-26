package de.jeezycore.commands.basic;

import com.google.common.base.Joiner;
import de.jeezycore.db.RanksSQL;
import de.jeezycore.events.chat.StaffChat;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Report implements CommandExecutor {
    RanksSQL display = new RanksSQL();
    StaffChat staffChat = new StaffChat();
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

                display.getPlayerInformation(p.getPlayer());
                String sql = "SELECT * FROM ranks WHERE rankName = '"+display.rankNameInformation+"'";
                display.displayChatRank(sql);

                String show_rank_color_reporter = display.rankColor.replace("&", "§");

                display.displayChatRank(sql);

                String show_rank_color_reported = display.rankColor_second.replace("&", "§");

                String report_msg = "§7§l[§4Report§7§l] ("+show_rank_color_reporter+p.getDisplayName()+"§6§l -> §7"+show_rank_color_reported+args[0]+"§7§l) §c§lReason§f§l: §7"+input;

                staffChat.reportChat(p, report_msg);

                p.sendMessage("§7You §a§lsuccessfully §7reported "+show_rank_color_reported+args[0]+"§7.\n" +
                        "§7§l► §9Thanks §7for §9§lcaring §7about our §9§lcommunity§7. §7§l[§4§l❤§7§l]");

            } else {
                p.sendMessage("Usage: /report <player><message>");
            }
        }

        return true;
    }
}
