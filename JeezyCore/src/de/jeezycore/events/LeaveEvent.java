package de.jeezycore.events;

import de.jeezycore.utils.MsgArray;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        MsgArray.reply_array.remove(e.getPlayer().getDisplayName());
        e.setQuitMessage("");
    }

}
