package de.jeezycore.commands.friends;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendsCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            try {
                switch (args[0]) {
                    case "add":
                        p.sendMessage("Execute add");
                        break;
                    case "remove":
                        p.sendMessage("Execute remove");
                        break;
                    default:
                        helpMessage(p);
                        break;
                }
            } catch (Exception e) {
                helpMessage(p);
            }
        }
        return true;
    }

    private void helpMessage(Player p) {
        p.getPlayer().sendMessage(new String[]{
                "                                                                       ",
                " §9§lFriends §f§lHelp",
                "                                                                       ",
                " §f/§9friends §fadd       - §7Adds a friend to your friend list.",
                " §f/§9friends §fremove  - §7Removes a friend from your friend list.",
                "                                                                       "
        });
    }
}