package de.jeezycore.discord.utils;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import java.awt.*;

public class MineralEmbeds {

    public MessageBuilder panelMessageBuilder() {
        return new MessageBuilder()
                .addComponents(
                        ActionRow.of(Button.success("success", "Restart"),
                                Button.danger("danger", "Shutdown"),
                                Button.secondary("secondary", "Whitelist"),
                                Button.primary("primary", "Maintenance")))
                .setEmbeds(panelEmbed());
    }

    public EmbedBuilder panelEmbed() {
        return new EmbedBuilder()
                .setTitle("Mineral -> Quick Control Panel")
                .setDescription("Use the **button(s)** below to execute an action.")
                .setColor(Color.decode("#3446d9"))
                .setFooter("Mineral Bot", "https://i.imgur.com/UlvkKpB.png");
    }
}