package de.jeezycore.events.chat;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.db.MuteSQL;
import de.jeezycore.db.TagsSQL;
import de.jeezycore.discord.messages.realtime.RealtimeChat;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

import static de.jeezycore.db.TagsSQL.tag_in_chat;

public class ChatEvent implements Listener {
    private String chatFormatMessage;

    @EventHandler
    public void onPlayerChat1(AsyncPlayerChatEvent e) {
        StaffChat staffChat = new StaffChat();
        staffChat.chat(e);
        JeezySQL display = new JeezySQL();
        String sql = "SELECT * FROM jeezycore WHERE playerName LIKE '%"+ e.getPlayer().getUniqueId().toString() +"%'";
        display.displayChatRank(sql);
        System.out.println(display.rank);
        System.out.println(display.woolColor);
        System.out.println(e.getPlayer().getUniqueId());
        TagsSQL tagsSQL = new TagsSQL();
        tagsSQL.tagChat(e.getPlayer().getUniqueId());

        RealtimeChat rmc = new RealtimeChat();
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

            if (tag_in_chat == null) {
                tag_in_chat = "";
            } else {
                tag_in_chat = " "+tag_in_chat;
            }
            if (display.rank == null) {
                display.rank = "";


                rmc.realtimeMcChat( e.getPlayer().getDisplayName()+": "+e.getMessage());
                MemorySection cf = (MemorySection) JeezyConfig.config_defaults.get("chat");
                String chat_format_rep = cf.getString("chat_format").replace("[rank]", display.rank).replace("&", "§").replace("[player]", "§2"+e.getPlayer().getDisplayName()).replace("[msg]", e.getMessage()).replace("[tag]", tag_in_chat).replace("&", "§");
                e.setFormat(chat_format_rep.replace("%", "%%").trim());
            } else {
                String show_color = display.rankColor;
                System.out.println(show_color);
                rmc.realtimeMcChat("["+display.rank+"]"+" "+e.getPlayer().getDisplayName()+": "+e.getMessage());
                MemorySection cf = (MemorySection) JeezyConfig.config_defaults.get("chat");
                String chat_format_rep = cf.getString("chat_format").replace("[rank]", "§7["+show_color+""+display.rank+"§7§l]§f").replace("&", "§").replace("[player]", "§f"+e.getPlayer().getDisplayName()).replace("[msg]", e.getMessage()).replace("[tag]", tag_in_chat.replace("&", "§"));
                e.setFormat(chat_format_rep.replace("%", "%%"));
            }
        tag_in_chat = null;

        try {
            MemorySection mc = (MemorySection) JeezyConfig.config_defaults.get("chat");
            boolean chat_muted = mc.getBoolean("muted");
            List<String> ignored_roles = (List<String>) mc.getList("ignored_roles_on_chat-mute");

            System.out.println(ignored_roles);

             if (!chat_muted || ignored_roles.contains(display.rank)) {
                System.out.println("Chat is enabled");
            } else {
               System.out.println("Chat is disabled!");
                e.getPlayer().sendMessage("§4§lChat has been disabled.");
                e.setCancelled(true);
            }


        } catch (Exception f) {
            f.printStackTrace();
        }




    }

}
