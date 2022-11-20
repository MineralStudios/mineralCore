package de.jeezycore.events.chat;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.db.StaffSQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class StaffChat {

    public void chat(AsyncPlayerChatEvent e) {
        String chat_message = e.getMessage();
        String firstChar = String.valueOf(chat_message.charAt(0));
        String expectedOutput = "@";

        StaffSQL staffSQL = new StaffSQL();
        staffSQL.checkIfStaff(UUID.fromString(e.getPlayer().getUniqueId().toString()));

        if (StaffSQL.staffRank && firstChar.equalsIgnoreCase(expectedOutput)) {
            staffSQL.getStaff();

                JeezySQL display = new JeezySQL();
                String sql = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ e.getPlayer().getUniqueId().toString() +"%'";
                display.displayChatRank(sql);

                try {
                    for (int i = 0; i < StaffSQL.staff.size(); i++) {
                        String new_message = e.getMessage().replace("@", "").trim();
                        Bukkit.getPlayer(UUID.fromString(StaffSQL.staff.get(i))).sendMessage("§7§l[§bStaff§7-§bChat§7§l] "+ColorTranslator.colorTranslator.get(display.rankColor)+e.getPlayer().getDisplayName()+"§f: "+new_message);
                        e.setCancelled(true);
                    }
                    StaffSQL.staffRank = false;
                    StaffSQL.staffPlayerNames = null;
                    StaffSQL.staff.clear();
                } catch (Exception f) {
                    f.printStackTrace();
                }
        }
    }

    public void helpopChat(Player p, String message) {
        StaffSQL staffSQL = new StaffSQL();
        staffSQL.getStaff();

        JeezySQL display = new JeezySQL();
        String sql = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ p.getPlayer().getUniqueId().toString() +"%'";
        display.displayChatRank(sql);

        try {
            for (int i = 0; i < StaffSQL.staff.size(); i++) {
                Bukkit.getPlayer(UUID.fromString(StaffSQL.staff.get(i))).sendMessage("§7§l[§4Help§7§cop§7§l] "+ColorTranslator.colorTranslator.get(display.rankColor)+p.getPlayer().getDisplayName()+"§f: "+message);
            }
            StaffSQL.staffRank = false;
            StaffSQL.staffPlayerNames = null;
            StaffSQL.staff.clear();
        } catch (Exception f) {
            f.printStackTrace();
        }
    }

}