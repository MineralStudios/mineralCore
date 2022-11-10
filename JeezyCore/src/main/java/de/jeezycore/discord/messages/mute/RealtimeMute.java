package de.jeezycore.discord.messages.mute;

import de.jeezycore.discord.JeezyBot;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class RealtimeMute {

    public void realtimeChatOnMute(UUID uuid_player, String uuid_name, String granter, String reason) {
        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById("1003984701993779210").get();
        Locale locale = new Locale("en", "US");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        // Create the embed
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("__Permanently Mute notification__")
                .setDescription("Muted the player **"+uuid_name+"**.")
                .setImage("https://crafatar.com/avatars/"+uuid_player)
                .addField("Muted by", granter)
                .addField("Reason", reason)
                .addInlineField("Muted player", uuid_name)
                .addInlineField("Mute_Start", date)
                .addField("Mute_End", "Never")
                .setColor(Color.MAGENTA);

        // Send the embed
        channel.sendMessage(embed);
    }

    public void realtimeChatOnTempMute(UUID uuid_player, String uuid_name, String granter, String ban_end, String reason) {
        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById("1003984701993779210").get();
        Locale locale = new Locale("en", "US");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        // Create the embed
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("__TempMute notification__")
                .setDescription("Muted the player **"+uuid_name+"**.")
                .setImage("https://crafatar.com/avatars/"+uuid_player)
                .addField("Muted by", granter)
                .addField("Reason", reason)
                .addInlineField("Muted player", uuid_name)
                .addInlineField("Mute_Start", date)
                .addField("Mute_End", ban_end)
                .setColor(Color.pink);

        // Send the embed
        channel.sendMessage(embed);
    }

    public void realtimeChatOnUnMute(UUID uuid_player, String uuid_name, String unMuter) {
        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById("1003984701993779210").get();
        Locale locale = new Locale("en", "US");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        // Create the embed
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("__Unban notification__")
                .setDescription("Unbanned the player **"+uuid_name+"**.")
                .setImage("https://crafatar.com/avatars/"+uuid_player)
                .addField("UnBanned by", unMuter)
                .addInlineField("UnBanned player", uuid_name)
                .addInlineField("Unban_Date", date)
                .setColor(Color.yellow);

        // Send the embed
        channel.sendMessage(embed);
    }

}
