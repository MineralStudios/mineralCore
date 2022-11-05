package de.jeezycore.commands.punishments.mute;

import com.google.common.base.Joiner;
import de.jeezycore.db.MuteSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("mute") && args.length > 1) {
                List<String> ls = new ArrayList<String>(Arrays.asList(args));
                String input = Joiner.on("")
                        .skipNulls()
                        .join(ls).replace(args[0], "");

                MuteSQL execute = new MuteSQL();
                execute.mute(args[0], input, p.getPlayer());

            } else {
                p.sendMessage("Usage: /mute <player><reason>.");
            }

        }
        return false;
    }
}
