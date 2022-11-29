package de.jeezycore.commands.tags;

import de.jeezycore.db.TagsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnGrantTag implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ungrant-tag") && args.length == 2) {
                if (p.hasPermission("jeezy.core.tag.ungrant")) {
                    TagsSQL tag = new TagsSQL();
                    tag.deleteTag(args[0], args[1], p);
                } else {
                    p.sendMessage("No permission");
                }
            } else {
                p.sendMessage("Usage: /ungrant-tag <tagName><player>");
            }
        }
        return true;
    }
}
