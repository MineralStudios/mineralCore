package de.jeezycore.commands.tags;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.TagsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateTag implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

           if (cmd.getName().equalsIgnoreCase("create-tag") && args.length < 3) {
              p.sendMessage("Usage: /create-tag (name) (design) (priority)");
           } else {
               if (p.hasPermission("jeezy.core.tags.create")) {
                   TagsSQL mySQL = new TagsSQL();
                   String input = "INSERT INTO tags " +
                           "(tagName, tagDesign, tagPriority) " +
                           "VALUES " +
                           "(?, ?, ?)";
                   mySQL.pushData(input, p, args[0], args[1], args[2]);
               } else {
                   p.sendMessage("No permission.");
               }
           }
        }
        return true;
    }
}