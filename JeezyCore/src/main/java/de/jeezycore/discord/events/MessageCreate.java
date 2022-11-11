package de.jeezycore.discord.events;


import de.jeezycore.config.JeezyConfig;
import de.jeezycore.discord.messages.realtime.RealtimeChat;
import org.bukkit.configuration.MemorySection;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;


public class MessageCreate implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        RealtimeChat discord_realtime = new RealtimeChat();
        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-realtime-chat");
        String id = (String) discord.get("discordToMc");

        if (event.getMessage().getUserAuthor().get().isBot() || event.getChannel().getId() != Long.parseLong(id)) {
            return;
        }
        discord_realtime.realtimeChatViaDiscord("§7[§9Discord§7]§f "+event.getMessage().getUserAuthor().get().getName()+"#"+event.getMessage().getUserAuthor().get().getDiscriminator()+": "+event.getMessageContent());
    }
}