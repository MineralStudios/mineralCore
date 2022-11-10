package de.jeezycore.discord.messages.ban;

import de.jeezycore.discord.JeezyBot;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class RealtimeBan {

    public void realtimeChatOnBan(UUID uuid_player, String uuid_name, String banner, String reason) {
        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById("1003984701993779210").get();
        Locale locale = new Locale("en", "US");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        // Create the embed
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("__Permanently Ban notification__")
                .setDescription("Banned the player **"+uuid_name+"**.")
                .setImage("https://crafatar.com/avatars/"+uuid_player)
                .addField("Banned by", banner)
                .addField("Reason", reason)
                .addInlineField("Banned player", uuid_name)
                .addInlineField("Ban_Start", date)
                .addField("Ban_End", "Never")
                .setColor(Color.red);

        // Send the embed
        channel.sendMessage(embed);
    }

    public void realtimeChatOnTempBan(UUID uuid_player, String uuid_name, String banner, String ban_end, String reason) {
        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById("1003984701993779210").get();
        Locale locale = new Locale("en", "US");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        // Create the embed
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("__TempBan notification__")
                .setDescription("TempBanned the player **"+uuid_name+"**.")
                .setImage("https://crafatar.com/avatars/"+uuid_player)
                .addField("Banned by", banner)
                .addField("Reason", reason)
                .addInlineField("Banned player", uuid_name)
                .addInlineField("Ban_Start", date)
                .addField("Ban_End", ban_end)
                .setColor(Color.LIGHT_GRAY);

        // Send the embed
        channel.sendMessage(embed);
    }

}
