package de.jeezycore.discord.events;


import de.jeezycore.discord.chat.RealtimeChat;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;


public class MessageCreate implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        RealtimeChat discord_realtime = new RealtimeChat();
        if (event.getMessage().getUserAuthor().get().isBot()) {
            return;
        }
        discord_realtime.realtimeChatViaDiscord("§7[§9Discord§7]§f "+event.getMessage().getUserAuthor().get().getName()+"#"+event.getMessage().getUserAuthor().get().getDiscriminator()+": "+event.getMessageContent());

    }
}
