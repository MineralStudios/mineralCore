package de.jeezycore.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;

public class CommandExecuteListener {

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        if (event.getCommand().contains("lpv")) {
            event.setResult(CommandExecuteEvent.CommandResult.forwardToServer("/commandnotfound"));
        }
    }

}
