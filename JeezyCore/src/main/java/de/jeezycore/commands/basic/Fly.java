package de.jeezycore.commands.basic;

import de.jeezycore.utils.PermissionHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static de.jeezycore.utils.ArrayStorage.fly_array;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;


            if (cmd.getName().equalsIgnoreCase("fly")) {
                if (p.hasPermission("jeezy.core.fly")) {

                    System.out.println(fly_array.contains(p.getPlayer().getDisplayName()));
                    System.out.println(fly_array);

                    if (!fly_array.contains(p.getPlayer().getDisplayName())) {
                        p.setAllowFlight(true);
                        p.setFlying(true);
                        p.sendMessage("Fly on");
                        fly_array.add(p.getPlayer().getDisplayName());
                    } else {
                        p.setAllowFlight(false);
                        p.setFlying(false);
                        p.sendMessage("Fly off");
                        fly_array.remove(p.getPlayer().getDisplayName());
                    }

                } else {
                    p.sendMessage("No permission.");
                }
            }



        }

        return true;
    }
}
