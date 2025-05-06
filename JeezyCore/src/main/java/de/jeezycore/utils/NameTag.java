package de.jeezycore.utils;

import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.TabListSQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import static de.jeezycore.utils.ArrayStorage.*;

public class NameTag {

    TabListSQL tabListSQL = new TabListSQL();

    public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private final RanksSQL givePermsOnJoin = new RanksSQL();

    public void giveTagOnJoin(Player player) {
            tabListSQL.getTabListData(player.getPlayer().getUniqueId());

            if (tab_name_list_array.get(player.getPlayer().getUniqueId()) != null) {
                scoreboard.getTeam(TabListSQL.getTabListPriority+""+TabListSQL.getTabListRanks).addEntry(player.getDisplayName());
            } else if (gettingNameMcLikesResponse.body().contains(player.getUniqueId().toString())) {
                scoreboard.getTeam("QNameMC").addEntry(player.getDisplayName());
                givePermsOnJoin.onJoinPerms("Mineral", player.getPlayer().getUniqueId());
            } else {
                scoreboard.getTeam("ZDefault").addEntry(player.getDisplayName());
            }
        }

   public void giveTagAfterMatch(Player player1, Player player2) {

/*
        if (tab_name_list_array.get(player1) != null) {
            //NametagEdit.getApi().setPrefix(player1, tab_name_list_array.get(player1));
            tabListSQL.getTabListData(player1);
            scoreboard.getTeam(RanksSQL.rankNameInformation).addEntry(tab_name_list_array.get(player1));
        } else {
            //NametagEdit.getApi().setPrefix(player1, "§2");
            scoreboard.getTeam("ZDefault").addEntry(player1.getDisplayName());
        }

        if (tab_name_list_array.get(player2) != null) {
            //NametagEdit.getApi().setPrefix(player2, tab_name_list_array.get(player2));
            scoreboard.getTeam(RanksSQL.rankNameInformation).addEntry(tab_name_list_array.get(player2));
        } else {
            //NametagEdit.getApi().setPrefix(player2, "§2");
            scoreboard.getTeam("ZDefault").addEntry(player2.getDisplayName());
        }
 */
    }
}