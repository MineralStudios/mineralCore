package de.jeezycore.commands.basic;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class Ping implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;


             if (cmd.getName().equalsIgnoreCase("ping") && args.length == 0) {
                try {
                    Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
                    int ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);

                    p.sendMessage(p.getDisplayName() + "´s"+" §9§lping: §7§l"+ ping+" §9§lms");

                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            } else if (cmd.getName().equalsIgnoreCase("ping") && args.length > 0) {
                 if (Bukkit.getServer().getPlayerExact(args[0]) == null) {
                     p.sendMessage("§4This player is not online.");
                     return true;
                 }
                try {
                    Object entityPlayer = Bukkit.getPlayer(args[0]).getClass().getMethod("getHandle").invoke(Bukkit.getPlayer(args[0]));
                    int ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);

                    p.sendMessage(args[0] + "´s"+" §9§lping: §7§l"+ ping+" §9§lms");
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
                e.printStackTrace();
                }

            }






        }

        return true;
    }
}
