package de.jeezycore.discord.commands;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MineralSlashCommands {

    public void initializeCommands(DiscordApi api) {
        Set<SlashCommandBuilder> builders = new HashSet<>();
        builders.add(new SlashCommandBuilder().setName("panel-setup").setDescription("Creates a control panel for our server.")
                        .addOption(SlashCommandOption.createChannelOption("channel", "Select the channel.", true, Collections.singleton(ChannelType.SERVER_TEXT_CHANNEL)))
                .setDefaultEnabledForPermissions(PermissionType.ADMINISTRATOR, PermissionType.BAN_MEMBERS)
                .setEnabledInDms(false)
        );

        api.bulkOverwriteGlobalApplicationCommands(builders).join();
    }
}