package de.jeezycore.commands.spawn;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("spawnSet") && args.length > 0) {
                p.sendMessage("Usage /spawnSet");
            } else {
                if (p.hasPermission("jeezy.core.spawn.setup")) {
                    p.sendMessage("§2§lYou successfully setuped the spawn.");
                    try {
                        Location worldObject = p.getLocation();

                        List<Location> locations = new ArrayList<>(Arrays.asList(worldObject));
                        JeezyConfig.config_defaults.set("entry-spawn-point", locations);
                        JeezyConfig.config_defaults.save(JeezyConfig.config);
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                } else {
                    p.sendMessage("No permission.");
                }
            }
        }
        return true;
    }
}