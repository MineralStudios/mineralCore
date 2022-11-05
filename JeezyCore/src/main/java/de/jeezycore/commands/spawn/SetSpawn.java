package de.jeezycore.commands.spawn;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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

            if (cmd.getName().equalsIgnoreCase("jeezy-spawn-set") && args.length > 0) {
                p.sendMessage("Usage /jeezy-spawn-set");
            } else {
                p.sendMessage("§2§lYou successfully setuped the spawn.");
        try {
            Location worldObject = p.getLocation();

            List<Location> locations = new ArrayList<>(Arrays.asList(worldObject));
            JeezyConfig.config_defaults.set("entry-spawn-point", locations);

            JeezyConfig.config_defaults.set("world", p.getPlayer().getLocation().getWorld().getName());
            JeezyConfig.config_defaults.set("x", p.getPlayer().getLocation().getBlockX());
            JeezyConfig.config_defaults.set("y", p.getPlayer().getLocation().getBlockY());
            JeezyConfig.config_defaults.set("z", p.getPlayer().getLocation().getBlockZ());
            JeezyConfig.config_defaults.set("pitch", p.getPlayer().getLocation().getPitch());
            JeezyConfig.config_defaults.set("yaw", p.getPlayer().getLocation().getYaw());

            JeezyConfig.config_defaults.save(JeezyConfig.config);
        } catch (Exception f) {
            f.printStackTrace();
        }
            }
        }
        return false;
    }
}