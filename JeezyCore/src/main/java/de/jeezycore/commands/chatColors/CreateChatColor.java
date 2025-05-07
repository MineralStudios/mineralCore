package de.jeezycore.commands.chatColors;

import de.jeezycore.db.ChatColorSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class CreateChatColor implements CommandExecutor {

    ChatColorSQL chatColorSQL = new ChatColorSQL();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.create.chatColor")) {
                if (cmd.getName().equalsIgnoreCase("create-chatColor") && args.length == 4) {
                    CompletableFuture.runAsync(() -> {
                        chatColorSQL.create(p, args[0], args[1].replace("&", "§"), args[2].replace(",", ", "), Integer.parseInt(args[3]));
                    });
                } else {
                    p.sendMessage("Usage: /create-chatColor <colorName> <color> <colorRGB> <colorPriority>");
                }
            } else {
                p.sendMessage("No permission");
            }
        }
        return true;
    }
}