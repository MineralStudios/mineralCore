package de.jeezycore.discord.events;


import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;


public class MessageCreate implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        System.out.println(event.getMessageContent());

    }

}
