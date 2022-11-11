package de.jeezycore.discord.messages.realtime;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.discord.JeezyBot;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.javacord.api.entity.channel.TextChannel;

public class RealtimeChat {

    public void realtimeMcChat(String msg) {
    try {
        MemorySection chat_channel = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-realtime-chat");
        MemorySection realtime_chat = (MemorySection) JeezyConfig.discord_defaults.get("discord-realtime-chat");
        String id = (String) chat_channel.get("mcToDiscord");
        boolean mc_to_discord_on = realtime_chat.getBoolean("mcToDiscord");

        if (!mc_to_discord_on) {
            return;
        }

        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById(Long.parseLong(id)).get();
        channel.sendMessage(msg);
    } catch (Exception e) {

    }
    }
    public void realtimeChatViaDiscord(String msg) {
        try {
            MemorySection realtime_chat = (MemorySection) JeezyConfig.discord_defaults.get("discord-realtime-chat");
            boolean discord_to_mc_on = realtime_chat.getBoolean("discordToMc");

            if (!discord_to_mc_on) {
                return;
            }

            Bukkit.getServer().broadcastMessage(msg);
        } catch (Exception e) {

        }
    }
}
