package de.jeezycore.discord.messages.grant;

import de.jeezycore.discord.JeezyBot;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class RealtimeGrant {

    public void realtimeChatOnGranting(UUID uuid_player, String uuid_name, String granter, String rankName) {
        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById("1003984701993779210").get();
        Locale locale = new Locale("en", "US");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        // Create the embed
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("__Grant notification__")
                .setDescription("Granted the **"+rankName+"** rank to "+uuid_name+".")
                .setImage("https://crafatar.com/avatars/"+uuid_player)
                .addField("Granted by", granter)
                .addInlineField("Granted player", uuid_name)
                .addInlineField("Date", date)
                .setColor(Color.GREEN);

        // Send the embed
        channel.sendMessage(embed);
    }

    public void realtimeChatOnUnGranting(UUID uuid_player, String uuid_name, String granter) {
        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById("1003984701993779210").get();
        Locale locale = new Locale("en", "US");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        // Create the embed
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("__Ungrant notification__")
                .setDescription("Removed the rank from "+uuid_name+".")
                .setImage("https://crafatar.com/avatars/"+uuid_player)
                .addField("UnGranted by", granter)
                .addInlineField("UnGranted player", uuid_name)
                .addInlineField("Date", date)
                .setColor(Color.RED);

        // Send the embed
        channel.sendMessage(embed);
    }

}
