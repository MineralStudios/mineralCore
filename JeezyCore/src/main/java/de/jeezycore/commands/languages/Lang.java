package de.jeezycore.commands.languages;

import de.jeezycore.events.inventories.languages.LanguagesInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Lang implements CommandExecutor {

    LanguagesInventory languagesInventory = new LanguagesInventory();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("lang") && args.length == 0) {
                languagesInventory.languagesMenu(p);
            } else {
                p.sendMessage("Usage: /lang");
            }
        }
        return true;
    }
}