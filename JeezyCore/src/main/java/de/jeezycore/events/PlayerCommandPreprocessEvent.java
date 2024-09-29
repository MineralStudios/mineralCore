package de.jeezycore.events;

import de.jeezycore.commands.friends.FriendsCommands;
import de.jeezycore.db.RanksSQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.BitSet;
import java.util.Map;

import static de.jeezycore.db.RanksSQL.rankAndColor;
import static de.jeezycore.utils.ArrayStorage.getUserRankHashMap;

public class PlayerCommandPreprocessEvent implements Listener {

    FriendsCommands friendsCommands = new FriendsCommands();
    StringBuilder availableRanks = new StringBuilder();
    StringBuilder onlinePlayers = new StringBuilder();
    RanksSQL ranksSQL = new RanksSQL();
    Integer playerCount = 0;

    @EventHandler
    public void onCommandExecute(org.bukkit.event.player.PlayerCommandPreprocessEvent e) {
       if (e.getMessage().contains("/help")) {
           e.setCancelled(true);
           e.getPlayer().sendMessage(new String[] {
                   "\n",
                   "\n",
                   " §9§lMineral §f§lPractice",
                   "\n",
                   " §71v1s, 2v2s, PvPBots, Duels, Parties, Events",
                   "\n",
                   " §9§l♦ §fTo play,§9§l right click with your sword",
                   "\n",
                   " §9§l♦ §fTo duel someone,§9§l /duel [player]",
                   "\n",
                   " §9§l♦ §fTo edit your kit,§9§l right click with your book",
           });
       } else if (e.getMessage().contains("/list")) {
           ranksSQL.getAllUsersWithRank();
           e.setCancelled(true);
           for (Map.Entry<String, String> entry : rankAndColor.entrySet()) {
               availableRanks.append(entry.getValue().replace('&', '§')).append(entry.getKey()).append("§f, ");
           }
           for (Player p : Bukkit.getOnlinePlayers()) {
               if (getUserRankHashMap.containsKey(p.getUniqueId())) {
                   onlinePlayers.append(rankAndColor.get(getUserRankHashMap.get(p.getUniqueId())).replace("&", "§")).append(p.getDisplayName()).append("§f, ");
               } else {
                   try {
                       if (p.getAddress().toString() != null) {
                           onlinePlayers.append("§2").append(p.getDisplayName()).append("§f, ");
                       }
                   } catch (Exception x) {
                       onlinePlayers.indexOf(p.getDisplayName().replace(p.getDisplayName(), ""));
                       playerCount -= 1;
                   }
               }
               playerCount += 1;
           }

           availableRanks.delete(availableRanks.length() -2, availableRanks.length());
           onlinePlayers.delete(onlinePlayers.length() -2, onlinePlayers.length());

           e.getPlayer().sendMessage(new String[] {
                   "\n",
                   " §f§lAvailable §9§lRanks§f:",
                   "\n",
                   " "+availableRanks,
                   "\n",
                   " §9§l"+playerCount+" §f§lPlayer§9§l(s) §f§lare §9§lOnline§f:",
                   "\n",
                   " "+onlinePlayers,
           });
           availableRanks.setLength(0);
           onlinePlayers.setLength(0);
           playerCount = 0;
       }

       /*
    if (e.getMessage().equalsIgnoreCase("/friend")) {
        e.setCancelled(true);
        friendsCommands.helpMessage(e.getPlayer());
    }
        */
    }
}