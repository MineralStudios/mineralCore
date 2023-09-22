package de.jeezycore.events.chat;

import de.jeezycore.db.SettingsSQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GlobalChat {

    SettingsSQL settingsSQL = new SettingsSQL();

    public void checkGlobalChat(AsyncPlayerChatEvent e) {
        for (Player ps : Bukkit.getOnlinePlayers()) {
            settingsSQL.getSettingsData(ps.getUniqueId());
            if (settingsSQL.playerUUID != null && !settingsSQL.settingsGlobalChat) {
                e.getRecipients().remove(ps);
            }
        }
    }
}