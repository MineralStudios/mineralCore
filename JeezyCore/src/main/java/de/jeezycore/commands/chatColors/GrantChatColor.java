package de.jeezycore.commands.chatColors;

import de.jeezycore.db.ChatColorSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GrantChatColor implements CommandExecutor {

    ChatColorSQL chatColorSQL = new ChatColorSQL();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("jeezy.core.grant.chatColor")) {
                if (cmd.getName().equalsIgnoreCase("grant-chatColor") && args.length == 2) {
                    chatColorSQL.grantChatColor(p, args[0], args[1]);
                } else {
                    p.sendMessage("Usage: /grant-chatColor <playerName> <chatColor>");
                }
            } else {
                p.sendMessage("No permission");
            }
        }
        return true;
    }
}