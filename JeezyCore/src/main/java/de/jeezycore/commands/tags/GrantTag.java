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
            if (p.hasPermission("jeezy.core.tags.grant")) {
            if (cmd.getName().equalsIgnoreCase("grant-tag") && args.length == 2) {

                    mySQL.grantTag(p, args[0], args[1]);

            } else {
                p.sendMessage("Usage: /grant-tag (tagName) (playerName)");
            }
            } else {
                p.sendMessage("No permission.");
            }
        } else {
            if (cmd.getName().equalsIgnoreCase("grant-tag") && args.length == 2) {
                mySQL.grantTagConsole(sender, args[0], args[1]);
            } else {
                sender.sendMessage("Usage: /grant-tag <tagName> <playerName>");
            }

        }
        return true;
    }
}
