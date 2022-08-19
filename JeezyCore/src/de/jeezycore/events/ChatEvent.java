package de.jeezycore.events;

import de.jeezycore.db.JeezySQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat1(AsyncPlayerChatEvent e) {
        JeezySQL display = new JeezySQL();
        display.displayData();
        if (display.player == null) return;
        if (display.player.equalsIgnoreCase(e.getPlayer().getName())) {
            e.setFormat("§7§l["+display.rankColor+""+display.rank+"§7§l]§f "+e.getPlayer().getDisplayName()+": "+e.getMessage());
        }

    }

}
