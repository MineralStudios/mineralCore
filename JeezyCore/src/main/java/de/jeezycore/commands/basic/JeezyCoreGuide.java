package de.jeezycore.commands.basic;

import com.google.common.base.Joiner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JeezyCoreGuide implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            List<String> ls = new ArrayList<String>();


            ls.add("§8§l§m-----------------------------------\n"+
                    "§b§lJeezy§9§lCore Help\n"+
                    "§f§lcreate-rank: §9§l<name> <color> <priority>\n" +
                    "§9Creates rank.\n" +
                    "§f§lgrant-rank: §9§l<player>\n" +
                    "§9Grants player a rank.\n" + "§8§l§m-----------------------------------");


            p.sendMessage(Joiner.on(" ").join(ls));


        }

        return true;
    }
}
