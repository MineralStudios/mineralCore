package de.jeezycore.commands.tags;

import de.jeezycore.db.cache.TagsCache;
import de.jeezycore.events.inventories.tags.TagsInventory;
import de.jeezycore.main.Main;
import de.jeezycore.utils.ArrayStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class Tags implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("tags") && args.length == 0) {
                CompletableFuture.runAsync(() -> {
                    TagsCache.getInstance().getAllTags();
                    TagsCache.getInstance().getAllPlayerTags();
                }).thenRun(() -> {
                    Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> {
                        ArrayStorage.tags_inv_array.put(p.getUniqueId(), 1);
                        TagsInventory tagsInventory = new TagsInventory();
                        tagsInventory.tags_menu(p);
                    });
                });
            } else {
             p.sendMessage("Usage: /tags");
            }
        }
        return true;
    }
}
