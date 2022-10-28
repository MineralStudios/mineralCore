package de.jeezycore.commands.punishments;

import com.google.common.base.Joiner;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.*;

public class Ban implements CommandExecutor {
    int s = 1;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ban") && args.length > 1) {
                List<String> ls = new ArrayList<String>(Arrays.asList(args));
                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls).replace(args[0], "");

                JeezySQL banSQL = new JeezySQL();
                banSQL.ban(args[0], input, p.getPlayer());

            } else {
                p.sendMessage("Usage: /ban <player><reason>.");
            }

        }


        return false;
    }
}
