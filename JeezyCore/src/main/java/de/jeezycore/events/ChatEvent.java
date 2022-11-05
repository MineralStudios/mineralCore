package de.jeezycore.events;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.BanSQL;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.db.MuteSQL;
import de.jeezycore.discord.chat.RealtimeChat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.util.List;

public class ChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat1(AsyncPlayerChatEvent e) {
        JeezySQL display = new JeezySQL();
        String sql = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ e.getPlayer().getUniqueId().toString() +"%'";
        display.displayChatRank(sql);
        System.out.println(display.rank);
        System.out.println(display.rankColor);
        System.out.println(e.getPlayer().getUniqueId());

        File file = new File("/home/jeffrey/IdeaProjects/JeezyCore/JeezyCore/src/main/java/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        MuteSQL check_if_banned = new MuteSQL();
        check_if_banned.muteData(e.getPlayer().getUniqueId());
        MuteSQL.punishment_UUID = null;
        if (MuteSQL.mute_forever) {
            e.getPlayer().sendMessage("§cYou are currently muted.\n " +
                    "§bDuration: §4forever.");
            MuteSQL.mute_forever = false;
            e.setCancelled(true);
            return;
        } else if (check_if_banned.mute_end != null) {
            check_if_banned.tempMuteDurationCalculate(e.getPlayer());
            e.setCancelled(true);
            return;
        }

            if (display.rank == null) {
                RealtimeChat rmc = new RealtimeChat();
                rmc.realtimeMcChat( e.getPlayer().getDisplayName()+": "+e.getMessage());

                MemorySection cf = (MemorySection) config.get("chat");
                String chat_format_rep = cf.getString("chat_format").replace("&", "§").replace("[player]", e.getPlayer().getDisplayName()).replace("[msg]", e.getMessage());
                e.setFormat(chat_format_rep.replace("%", "%%"));
                return;
            }


        try {
            MemorySection mc = (MemorySection) config.get("chat");
            boolean chat_muted = mc.getBoolean("muted");
            List<String> ignored_roles = (List<String>) mc.getList("ignored_roles_on_chat-mute");

            System.out.println(ignored_roles);

             if (!chat_muted || ignored_roles.contains(display.rank)) {
                System.out.println("Chat is enabled");
            } else {
               System.out.println("Chat is disabled!");
                e.getPlayer().sendMessage("§4§lChat has been disabled.");
                e.setCancelled(true);
                return;
            }


        } catch (Exception f) {
            f.printStackTrace();
        }

            String show_color = ColorTranslator.colorTranslator.get(display.rankColor);
            System.out.println(show_color);
            RealtimeChat rmc = new RealtimeChat();
            rmc.realtimeMcChat("["+display.rank+"]"+" "+e.getPlayer().getDisplayName()+": "+e.getMessage());
            e.setFormat("§7§l["+show_color+""+display.rank+"§7§l]§f "+e.getPlayer().getDisplayName()+": "+e.getMessage().replace("%", "%%"));


    }

}
