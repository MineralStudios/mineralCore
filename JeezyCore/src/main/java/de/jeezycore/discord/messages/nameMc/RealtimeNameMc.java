package de.jeezycore.discord.messages.nameMc;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.discord.JeezyBot;
import org.bukkit.configuration.MemorySection;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import java.awt.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class RealtimeNameMc {

    public void nameMcNewVoterNotification(String uuid_player, Integer serverLikes) {
        MemorySection discord = (MemorySection) JeezyConfig.discord_defaults.get("discord-nameMc-notifications");
        MemorySection discord_support = (MemorySection) JeezyConfig.discord_defaults.get("discord-support");

        String id = (String) discord.get("onVote");
        boolean activated = (boolean) discord_support.get("activated");

        if (!activated) {
            return;
        }

        TextChannel channel = (TextChannel) JeezyBot.api.getChannelById(Long.parseLong(id)).get();
        Locale locale = new Locale("en", "US");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        // Create the embed
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("__Vote notification__")
                .setDescription("**We received a new vote on NameMc!**")
                .setImage("https://mineskin.eu/helm/"+uuid_player)
                .addInlineField("All Server Likes", String.valueOf(serverLikes))
                .addInlineField("Date", date)
                .setColor(Color.YELLOW);

        // Send the embed
        channel.sendMessage(embed);
    }
}