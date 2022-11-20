package de.jeezycore.commands.basic;

import com.google.common.base.Joiner;
import de.jeezycore.events.chat.StaffChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Helpop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("helpop") && args.length >= 1) {

            if (p.hasPermission("jeezy.core.staff.helpop")) {
                StaffChat staffChat = new StaffChat();

                List<String> ls = new ArrayList<String>(Arrays.asList(args));
                String input = Joiner.on(" ")
                        .skipNulls()
                        .join(ls).trim();

                staffChat.helpopChat(p, input);
            } else {
                p.sendMessage("You have no permission!");
            }
        } else {
            p.sendMessage("Usage: /helpop <message>");
        }
        }
        return true;
    }
}