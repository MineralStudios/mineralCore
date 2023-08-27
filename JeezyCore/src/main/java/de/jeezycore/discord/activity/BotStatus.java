package de.jeezycore.discord.activity;

import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.user.UserStatus;

import static de.jeezycore.discord.JeezyBot.api;

public class BotStatus {

    public void set () {
        api.updateStatus(UserStatus.DO_NOT_DISTURB);
        api.updateActivity(ActivityType.WATCHING,"► mineral.gg ◄");
    }
}