package de.jeezycore.commands.punishments;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnBan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("unban") && args.length > 0) {
                BanList ban_list_name = Bukkit.getBanList(BanList.Type.NAME);
                BanList ban_list_ip =  Bukkit.getBanList(BanList.Type.IP);

                ban_list_name.pardon(args[0]);
            } else {
                p.sendMessage("Usage: /unban <player>.");
            }
        }

        return false;
    }
}
