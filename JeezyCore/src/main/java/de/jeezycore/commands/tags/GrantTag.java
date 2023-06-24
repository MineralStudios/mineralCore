package de.jeezycore.commands.tags;

import de.jeezycore.db.TagsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GrantTag implements CommandExecutor {

    TagsSQL mySQL = new TagsSQL();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("grant-tag") && args.length == 2) {
                if (p.hasPermission("jeezy.core.tags.grant")) {

                    String input = "INSERT INTO tags " +
                            "(tagName, tagDesign, tagPriority) " +
                            "VALUES " +
                            "(?, ?, ?)";

                    mySQL.grantTag(p, args[0], args[1]);
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
                p.sendMessage("Usage: /grant-tag (tagName) (playerName)");
            }
        } else {
            mySQL.grantTagConsole(sender, args[0], args[1]);
        }
        return true;
    }
}
