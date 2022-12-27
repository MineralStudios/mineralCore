package de.jeezycore.discord.activity;

import static de.jeezycore.discord.JeezyBot.api;

public class BotStatus {

    public void set () {
        api.updateActivity("► mineral.gg ◄");
    }

}
