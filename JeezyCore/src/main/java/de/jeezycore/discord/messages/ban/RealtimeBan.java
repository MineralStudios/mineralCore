package de.jeezycore.discord.messages.ban;

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

public class RealtimeBan {

    public void realtimeChatOnBan(UUID uuid_player, String uuid_name, String banner, String reason) {
        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-banning");
        MemorySection discord_support = (MemorySection) JeezyConfig.discord_defaults.get("discord-support");
        MemorySection ban_notify = (MemorySection) JeezyConfig.discord_defaults.get("discord-banning-notifications");

        String id = (String) discord.get("onBan");
        boolean activated = (boolean) discord_support.get("activated");
        boolean ban_notify_on = ban_notify.getBoolean("onBan");

        if (!ban_notify_on | !activated) {
            return;
        }

        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById(Long.parseLong(id)).get();
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
        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-banning");
        MemorySection discord_support = (MemorySection) JeezyConfig.discord_defaults.get("discord-support");
        MemorySection tempBan_notify = (MemorySection) JeezyConfig.discord_defaults.get("discord-banning-notifications");

        String id = (String) discord.get("onTempBan");
        boolean tempBan_notify_on = tempBan_notify.getBoolean("onTempBan");
        boolean activated = (boolean) discord_support.get("activated");

        if (!tempBan_notify_on | !activated) {
            return;
        }

        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById(Long.parseLong(id)).get();
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

    public void realtimeChatOnUnban(UUID uuid_player, String uuid_name, String unBanner) {
        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-banning");
        MemorySection discord_support = (MemorySection) JeezyConfig.discord_defaults.get("discord-support");
        MemorySection UnBan_notify = (MemorySection) JeezyConfig.discord_defaults.get("discord-banning-notifications");

        String id = (String) discord.get("onUnBan");
        boolean UnBan_notify_on = UnBan_notify.getBoolean("onUnBan");
        boolean activated = (boolean) discord_support.get("activated");

        if (!UnBan_notify_on | !activated) {
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
                .addField("UnBanned by", unBanner)
                .addInlineField("UnBanned player", uuid_name)
                .addInlineField("Unban_Date", date)
                .setColor(Color.green);

        // Send the embed
        channel.sendMessage(embed);
    }
}
