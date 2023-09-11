package de.jeezycore.velocity.events;

import com.velocitypowered.api.event.Subscribe;

public class CommandExecuteEvent {

    @Subscribe
    public void onCommandExecute(com.velocitypowered.api.event.command.CommandExecuteEvent event) {
        if (event.getCommand().contains("lpv")) {
            event.setResult(com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult.forwardToServer("/commandnotfound"));
        }
    }
}