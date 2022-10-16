package de.jeezycore.discord.chat;

import de.jeezycore.discord.JeezyBot;
import org.javacord.api.entity.channel.TextChannel;

public class RealtimeChat {

    public void realtimeMcChat(String msg) {
        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById("1003984701993779210").get();
        channel.sendMessage(msg);
    }
}
