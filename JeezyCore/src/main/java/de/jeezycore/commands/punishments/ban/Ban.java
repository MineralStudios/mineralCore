package de.jeezycore.commands.punishments.ban;

import com.google.common.base.Joiner;
import de.jeezycore.db.BanSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Ban implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ban") && args.length > 1) {
                if (p.hasPermission("jeezy.core.punishments.ban")) {
                    List<String> ls = new ArrayList<String>(Arrays.asList(args));
                    String input = Joiner.on("")
                            .skipNulls()
                            .join(ls).replace(args[0], "");

                    BanSQL execute = new BanSQL();
                    execute.ban(args[0], input, p.getPlayer());
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
                p.sendMessage("Usage: /ban <player><reason>.");
            }
        }
        return false;
    }
}