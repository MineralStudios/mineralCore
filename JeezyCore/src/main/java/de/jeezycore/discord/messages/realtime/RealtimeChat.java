package de.jeezycore.discord.messages.realtime;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.discord.JeezyBot;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.javacord.api.entity.channel.TextChannel;

public class RealtimeChat {

    public void realtimeMcChat(String msg) {

        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-realtime-chat");
        String id = (String) discord.get("mcToDiscord");

        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById(Long.parseLong(id)).get();
        channel.sendMessage(msg);
    }
    public void realtimeChatViaDiscord(String msg) {
        Bukkit.getServer().broadcastMessage(msg);
    }
}
