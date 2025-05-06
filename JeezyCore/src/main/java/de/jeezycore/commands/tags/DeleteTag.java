package de.jeezycore.commands.tags;

import de.jeezycore.db.TagsSQL;
import de.jeezycore.db.cache.TagsCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.concurrent.CompletableFuture;

public class DeleteTag implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.tags.delete")) {
            if (cmd.getName().equalsIgnoreCase("delete-tag") && args.length == 1) {
                CompletableFuture.runAsync(() -> {
                    TagsSQL tagsSQL = new TagsSQL();
                    tagsSQL.deleteTag(args[0], p);
                    TagsCache.getInstance().reloadAllTagsNow();
                });
            } else {
                p.sendMessage("Usage: /delete-tag <tagName>");
            }
            } else {
                p.sendMessage("No permission.");
            }
        }
        return true;
    }
}