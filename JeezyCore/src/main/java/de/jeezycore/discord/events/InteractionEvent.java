package de.jeezycore.discord.events;

import de.jeezycore.discord.JeezyBot;
import de.jeezycore.discord.utils.MineralEmbeds;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.event.interaction.InteractionCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.InteractionCreateListener;
import java.util.Optional;

public class InteractionEvent implements InteractionCreateListener {

    private final MineralEmbeds mineralEmbeds = new MineralEmbeds();

    @Override
    public void onInteractionCreate(InteractionCreateEvent event) {
        Optional<SlashCommandInteraction> interaction = event.getSlashCommandInteraction();

        if (interaction.isPresent()) {
            if (interaction.get().getFullCommandName().equals("panel-setup")) {
                event.getInteraction().createImmediateResponder()
                        .setContent("The **Mineral -> quick control panel** has been **successfully** set up!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
            interaction.flatMap(interactionData -> interactionData.getOptions().get(0).getChannelValue()).ifPresent(channelValue -> {
                TextChannel userSelectedChannel = (TextChannel) JeezyBot.api
                        .getChannelById(channelValue.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Channel not found!"));
                    mineralEmbeds.panelMessageBuilder().send(userSelectedChannel);
            });
        }
    }
}