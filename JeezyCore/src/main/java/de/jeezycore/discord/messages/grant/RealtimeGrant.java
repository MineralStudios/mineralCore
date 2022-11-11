package de.jeezycore.discord.messages.grant;

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

public class RealtimeGrant {

    public void realtimeChatOnGranting(UUID uuid_player, String uuid_name, String granter, String rankName) {
        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-granting");
        MemorySection grant_notify = (MemorySection) JeezyConfig.discord_defaults.get("discord-granting-notifications");

        String id = (String) discord.get("onGranting");
        boolean on_granting_on = grant_notify.getBoolean("onGranting");

        if (!on_granting_on) {
            return;
        }

        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById(Long.parseLong(id)).get();
        Locale locale = new Locale("en", "US");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        // Create the embed
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("__Grant notification__")
                .setDescription("Granted the **"+rankName+"** rank to **"+uuid_name+"**.")
                .setImage("https://crafatar.com/avatars/"+uuid_player)
                .addField("Granted by", granter)
                .addInlineField("Granted player", uuid_name)
                .addInlineField("Date", date)
                .setColor(Color.GREEN);

        // Send the embed
        channel.sendMessage(embed);
    }

    public void realtimeChatOnUnGranting(UUID uuid_player, String uuid_name, String granter) {
        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-text-channels-granting");
        MemorySection unGrant_notify = (MemorySection) JeezyConfig.discord_defaults.get("discord-granting-notifications");

        String id = (String) discord.get("onUnGranting");
        boolean un_granting_on = unGrant_notify.getBoolean("onUnGranting");

        if (!un_granting_on) {
            return;
        }

        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById(Long.parseLong(id)).get();
        Locale locale = new Locale("en", "US");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        // Create the embed
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("__Ungrant notification__")
                .setDescription("Removed the rank from **"+uuid_name+"**.")
                .setImage("https://crafatar.com/avatars/"+uuid_player)
                .addField("UnGranted by", granter)
                .addInlineField("UnGranted player", uuid_name)
                .addInlineField("Date", date)
                .setColor(Color.RED);

        // Send the embed
        channel.sendMessage(embed);
    }

}
