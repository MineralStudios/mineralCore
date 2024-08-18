package de.jeezycore.discord.sync;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;

import java.util.concurrent.ExecutionException;

import static de.jeezycore.discord.JeezyBot.api;

public class Unlink {


    public void removeRole(String userID) {

        MemorySection discord_support = (MemorySection) JeezyConfig.discord_defaults.get("discord-support");
        boolean activated = (boolean) discord_support.get("activated");

        if (!activated) {
            return;
        }

        api.getRolesByName("Linked").stream().findAny().ifPresent(role -> {
            try {
                api.getUserById(userID).get().removeRole(role);
            } catch (Exception ignored) {
            }
        });
    }
}