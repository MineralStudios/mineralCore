package de.jeezycore.discord.events;

import de.jeezycore.commands.basic.Reboot;
import de.jeezycore.discord.db.MaintenanceSQL;
import de.jeezycore.discord.db.WhitelistedSQL;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;

public class MessageComponentCreate implements MessageComponentCreateListener {

    private final Reboot reboot = new Reboot();
    private final WhitelistedSQL whitelistedSQL = new WhitelistedSQL();
    private final MaintenanceSQL maintenanceSQL = new MaintenanceSQL();

    @Override
    public void onComponentCreate(MessageComponentCreateEvent event) {
        MessageComponentInteraction messageComponentInteraction = event.getMessageComponentInteraction();
        String customId = messageComponentInteraction.getCustomId();

        switch (customId) {
            case "success":
                messageComponentInteraction.createImmediateResponder()
                        .setContent("You **successfully** restarted the server!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
                reboot.rebootServer();
                break;
            case "danger":
                messageComponentInteraction.createImmediateResponder()
                        .setContent("You **successfully** shutdown the server!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
                reboot.shutDownServer();
                break;
            case "secondary":
                String whitelistStatus;
                whitelistedSQL.getWhitelistedData();
                if (WhitelistedSQL.whitelisted) {
                    whitelistedSQL.disableWhitelisted();
                    whitelistStatus = "off";
                } else {
                    whitelistedSQL.enableWhitelisted();
                    whitelistStatus = "on";
                }

                messageComponentInteraction.createImmediateResponder()
                        .setContent("You **successfully** turned "+whitelistStatus+" the whitelist!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();

                break;
            case "primary":
                String maintenanceStatus;
                maintenanceSQL.getMaintenanceData();
                if (MaintenanceSQL.maintenance) {
                    maintenanceSQL.disableMaintenance();
                    maintenanceStatus = "off";
                } else {
                    maintenanceSQL.enableMaintenance();
                    maintenanceStatus = "on";
                }

                messageComponentInteraction.createImmediateResponder()
                        .setContent("You **successfully** turned "+maintenanceStatus+" the maintenance!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();

                break;
        }
    }
}
