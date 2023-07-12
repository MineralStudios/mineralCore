package de.jeezycore.events.chat;

import de.jeezycore.db.MuteSQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class BanChat implements Listener {
    MuteSQL check_if_banned = new MuteSQL();
    @EventHandler
    public void onPlayerChatBan(AsyncPlayerChatEvent e) {

        check_if_banned.muteData(e.getPlayer().getUniqueId());
        MuteSQL.punishment_UUID = null;
        if (MuteSQL.mute_forever) {
            e.getPlayer().sendMessage("§7You are §4permanently §7muted.\n " +
                    "§7Duration: §9forever§7.");
            MuteSQL.mute_forever = false;
            e.setCancelled(true);
        } else if (check_if_banned.mute_end != null) {
            check_if_banned.tempMuteDurationCalculate(e.getPlayer());
            e.setCancelled(true);
        }
    }
}
