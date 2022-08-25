package de.jeezycore.events;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat1(AsyncPlayerChatEvent e) {
        JeezySQL display = new JeezySQL();
        String sql = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ e.getPlayer().getUniqueId().toString().replace("-", "") +"%'";
        display.displayChatRank(sql);
        System.out.println(display.rank);
        System.out.println(display.rankColor);
        System.out.println(e.getPlayer().getUniqueId());

            if (display.rank == null) return;

            String show_color = ColorTranslator.colorTranslator.get(display.rankColor);
            System.out.println(show_color);
            e.setFormat("§7§l["+show_color+""+display.rank+"§7§l]§f "+e.getPlayer().getDisplayName()+": "+e.getMessage().replace("%", "%%"));


    }

}
