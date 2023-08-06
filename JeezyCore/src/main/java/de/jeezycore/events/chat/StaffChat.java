package de.jeezycore.events.chat;

import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.StaffSQL;
import de.jeezycore.utils.BungeeChannelApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.UUID;


public class StaffChat {
    RanksSQL display = new RanksSQL();
    StaffSQL staffSQL = new StaffSQL();
    RanksSQL playerInfo = new RanksSQL();

    BungeeChannelApi bungeeChannelApi = new BungeeChannelApi();

    public void chat(AsyncPlayerChatEvent e) {
        String chat_message = e.getMessage();
        String firstChar = String.valueOf(chat_message.charAt(0));
        String expectedOutput = "@";

        playerInfo.getPlayerInformation(e.getPlayer());
        staffSQL.checkIfStaff(playerInfo.rankNameInformation);


        if (StaffSQL.staffRank && firstChar.equalsIgnoreCase(expectedOutput)) {
                    e.setCancelled(true);
                    bungeeChannelApi.sendStaffMessages(e);
                    StaffSQL.staff.clear();
                    StaffSQL.staffRankNamesArray.clear();
        }
    }

    public void helpopChat(Player p, String message) {
        playerInfo.getPlayerInformation(p.getPlayer());
        staffSQL.checkIfStaff(playerInfo.rankNameInformation);

        if (StaffSQL.staffRank) {
            staffSQL.getStaff();

            display.getPlayerInformation(p.getPlayer());
            String sql = "SELECT * FROM ranks WHERE rankName = '"+display.rankNameInformation+"'";
            display.displayChatRank(sql);


            for (int i = 0; i < StaffSQL.staff.size(); i++) {

                try {
                    if (!Bukkit.getPlayer(UUID.fromString(StaffSQL.staff.get(i))).isOnline()) {
                        continue;
                    }
                    Bukkit.getPlayer(UUID.fromString(StaffSQL.staff.get(i))).sendMessage("§7§l[§4Help§7§cop§7§l] "+display.rankColor.replace("&", "§")+p.getPlayer().getDisplayName()+"§f: "+message);

                } catch (Exception f) {
                }
            }
            StaffSQL.staff.clear();
            StaffSQL.staffRankNamesArray.clear();

        }
    }

    public void reportChat(Player p, String message) {
        staffSQL.getStaff();

        display.getPlayerInformation(p.getPlayer());
        String sql = "SELECT * FROM ranks WHERE rankName = '"+display.rankNameInformation+"'";
        display.displayChatRank(sql);


        for (int i = 0; i < StaffSQL.staff.size(); i++) {

            try {
                if (!Bukkit.getPlayer(UUID.fromString(StaffSQL.staff.get(i))).isOnline()) {
                    continue;
                }
                Bukkit.getPlayer(UUID.fromString(StaffSQL.staff.get(i))).sendMessage(message);

            } catch (Exception f) {
            }
        }
        StaffSQL.staff.clear();
        StaffSQL.staffRankNamesArray.clear();
    }
}