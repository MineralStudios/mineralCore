package de.jeezycore.commands.punishments.ban;

import com.google.common.base.Joiner;
import de.jeezycore.db.BanSQL;
import de.jeezycore.db.JeezySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TempBan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("tempban") && args.length >= 3) {
                if (p.hasPermission("jeezy.core.punishments.tempban")) {

                    List<String> ls = new ArrayList<String>(Arrays.asList(args));
                    String input = Joiner.on(" ")
                            .skipNulls()
                            .join(ls).replace(args[0], "").replace(args[1], "").replaceAll("\\s+", " ").trim();

                    BanSQL execute = new BanSQL();

                    execute.tempBan(args[0], args[1], input, p);
                    System.out.println(input);
                    System.out.println(ls);
                } else {
                    p.sendMessage("No permission.");
                }
            } else {
                p.sendMessage("Usage: /tempban <player><time><reason>");
            }
        }
        return true;
    }
}