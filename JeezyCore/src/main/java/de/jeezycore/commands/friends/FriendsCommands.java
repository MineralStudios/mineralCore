package de.jeezycore.commands.friends;

import de.jeezycore.db.FriendsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendsCommands implements CommandExecutor {

    FriendsSQL friendsSQL = new FriendsSQL();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            try {
                switch (args[0]) {
                    case "accept":
                        if (args.length == 2) {
                            if (p.getDisplayName().equalsIgnoreCase(args[1])) {
                                p.sendMessage("§7You can't §caccept §7yourself as a §9friend§7!");
                                return true;
                            }
                            friendsSQL.acceptFriends(p, args[1]);
                        } else {
                            helpMessage(p);
                        }
                        break;
                    case "add":
                        if (args.length == 2) {
                            if (p.getDisplayName().equalsIgnoreCase(args[1])) {
                                p.sendMessage("§7You can't §cadd §7yourself as a §9friend§7!");
                                return true;
                            }
                            friendsSQL.addFriends(p, args[1]);
                        } else {
                            helpMessage(p);
                        }
                        break;
                    case "remove":
                        if (args.length == 2) {
                            if (p.getDisplayName().equalsIgnoreCase(args[1])) {
                                p.sendMessage("§7You can't §cremove §7yourself as a §9friend§7!");
                                return true;
                            }
                            friendsSQL.removeFriends(p, args[1]);
                        } else {
                            helpMessage(p);
                        }
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
                " §f/§9friends §fadd §7<player> §f- §7Adds a friend to your friends list.",
                " §f/§9friends §fremove §7<player> §f- §7Removes a friend from your friends list.",
                "                                                                       "
        });
    }
}