package de.jeezycore.discord.messages.mute;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.discord.JeezyBot;
import org.bukkit.configuration.MemorySection;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class RealtimeMute {

    public void realtimeChatOnMute(UUID uuid_player, String uuid_name, String granter, String reason) {
        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-muting");
        MemorySection mute_notify = (MemorySection) JeezyConfig.discord_defaults.get("discord-granting-notifications");

        String id = (String) discord.get("onMute");
        boolean mute_notify_on = mute_notify.getBoolean("onMute");

        if (!mute_notify_on) {
            return;
        }

        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById(Long.parseLong(id)).get();
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
        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-muting");
        MemorySection tempMute_notify = (MemorySection) JeezyConfig.discord_defaults.get("discord-granting-notifications");

        String id = (String) discord.get("onTempMute");
        boolean tempMute_notify_on = tempMute_notify.getBoolean("onTempMute");

        if (!tempMute_notify_on) {
            return;
        }

        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById(Long.parseLong(id)).get();
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
        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-muting");
        MemorySection UnMute_notify = (MemorySection) JeezyConfig.discord_defaults.get("discord-granting-notifications");

        String id = (String) discord.get("onUnMute");
        boolean unMute_notify_on = UnMute_notify.getBoolean("onUnMute");

        if (!unMute_notify_on) {
            return;
        }

        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById(Long.parseLong(id)).get();
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
