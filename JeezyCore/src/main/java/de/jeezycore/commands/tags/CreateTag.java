package de.jeezycore.commands.tags;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.TagsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateTag implements CommandExecutor {

    List<Object> tag_categories_list = (List<Object>) JeezyConfig.config_defaults.get("tags.categories");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

           if (cmd.getName().equalsIgnoreCase("create-tag") && args.length < 3) {
              p.sendMessage("Usage: /create-tag (name) (category) (design) (priority)");
           } else {
               if (p.hasPermission("jeezy.core.tags.create")) {
                   TagsSQL mySQL = new TagsSQL();
                   String input = "INSERT INTO tags " +
                           "(tagName, tagCategory, tagDesign, tagPriority) " +
                           "VALUES " +
                           "(?, ?, ?, ?)";
                   if (tag_categories_list.contains(args[1])) {
                       mySQL.pushData(input, p, args[0], args[1], args[2].replace("&", "§"), args[3]);
                   } else {
                       p.sendMessage("The Category you chose doesn't exist.");
                       return true;
                   }
               } else {
                   p.sendMessage("No permission.");
               }
           }
        }
        return true;
    }
}