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

            if (cmd.getName().equalsIgnoreCase("friends") && args.length == 0) {
                helpMessage(p);
                return true;
            }

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
                            friendsSQL.removeFriendsBothParties(p, args[1]);
                        } else {
                            helpMessage(p);
                        }
                        break;
                    case "list":
                        if (args.length == 2) {
                            if (isNumeric(args[1]) && !args[1].equalsIgnoreCase("0")) {
                                friendsSQL.listFriends(p, Integer.parseInt(args[1]));
                            } else {
                                p.sendMessage("§7You need to enter a §cvalid §7number, starting from the number §91§7!");
                            }
                        } else {
                            helpMessage(p);
                        }
                        break;
                    case "enable":
                        if (args.length == 1) {
                            friendsSQL.friendsSwitcherMYSQL(p, String.valueOf(true), "§2enabled");
                        } else {
                            helpMessage(p);
                        }
                        break;
                    case "disable":
                        if (args.length == 1) {
                            friendsSQL.friendsSwitcherMYSQL(p, String.valueOf(false), "§cdisabled");
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
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void helpMessage(Player p) {
        p.getPlayer().sendMessage(new String[]{
                "                                                                       ",
                " §9§lFriends §f§lHelp",
                "                                                                       ",
                " §f/§9friends §fadd §7<player> §f- §7Adds a friend to your friends list.",
                " §f/§9friends §fremove §7<player> §f- §7Removes a friend from your friends list.",
                " §f/§9friends §flist §7<page> §f- §7Lists all friends you have.",
                " §f/§9friends §fenable §f- §7Enables you from receiving friends requests.",
                " §f/§9friends §fdisable §f- §7Stops you from receiving friends requests.",
                "                                                                       "
        });
    }
}