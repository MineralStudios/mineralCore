package de.jeezycore.commands.tags;

import de.jeezycore.db.TagsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnGrantTag implements CommandExecutor {

    TagsSQL tag = new TagsSQL();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ungrant-tag") && args.length == 2) {
                if (p.hasPermission("jeezy.core.tag.ungrant")) {

                    tag.unGrantTag(args[0], args[1], p);
                    tag.resetTag(args[0], args[1], p);
                } else {
                    p.sendMessage("No permission");
                }
            } else {
                p.sendMessage("Usage: /ungrant-tag <tagName><player>");
            }
        } else {
            if (cmd.getName().equalsIgnoreCase("ungrant-tag") && args.length == 2) {
                tag.unGrantTagConsole(args[0], args[1], sender);
                tag.resetTagConsole(args[0], args[1], sender);
            } else {
                sender.sendMessage("Usage: /ungrant-tag <tagName> <playerName>");
            }

        }
        return true;
    }
}
