package de.jeezycore.events;

import de.jeezycore.utils.ArrayStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import static de.jeezycore.utils.ArrayStorage.friendsAddList;

public class LeaveEvent implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        ArrayStorage.reply_array.remove(e.getPlayer().getDisplayName());
        ArrayStorage.tags_inv_array.remove(e.getPlayer().getUniqueId());
        ArrayStorage.tagsCheckStatus.remove(e.getPlayer().getUniqueId());
        ArrayStorage.playTimeHashMap.remove(e.getPlayer().getUniqueId());
        System.out.println(friendsAddList);
        e.setQuitMessage("");
    }

}
