package de.jeezycore.commands.languages;

import de.jeezycore.utils.ArrayStorage;
import net.suuft.libretranslate.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Languages implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("languages") && args.length == 0) {
                p.sendMessage(" §7Here are all §9§lavailable§7 languages: ");
                p.sendMessage("                                             ");
                for (Language i : ArrayStorage.languageMap.values()) {
                    if (i.toString().equalsIgnoreCase("NONE")) {
                        continue;
                    }
                    p.sendMessage(" §9§l"+i);
                    p.sendMessage("                                             ");
                }
                    p.sendMessage("§7With §9/lang §7you can set your §9language §7of choice.");
            } else {
                p.sendMessage("Usage: /languages");
            }

        }

        return true;
    }
}
