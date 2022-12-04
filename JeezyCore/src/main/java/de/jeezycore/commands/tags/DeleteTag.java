package de.jeezycore.commands.tags;

import de.jeezycore.db.TagsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteTag implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("delete-tag") && args.length == 1) {
                if (p.hasPermission("jeezy.core.tags.delete")) {
                    TagsSQL tagsSQL = new TagsSQL();
                    tagsSQL.deleteTag(args[0], p);
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
                p.sendMessage("Usage: /delete-tag <tagName>");
            }
        }
        return true;
    }
}