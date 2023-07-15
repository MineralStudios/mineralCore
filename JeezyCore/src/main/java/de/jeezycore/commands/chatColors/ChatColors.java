package de.jeezycore.commands.chatColors;

import de.jeezycore.events.inventories.chatColors.ChatColorsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatColors implements CommandExecutor {

    ChatColorsInventory chatColorsInventory = new ChatColorsInventory();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("chatColors") && args.length == 0) {
                chatColorsInventory.chatColorsMenu(p);
            } else {
                p.sendMessage("Usage: /chatColor");
            }
        }

        return true;
    }
}
