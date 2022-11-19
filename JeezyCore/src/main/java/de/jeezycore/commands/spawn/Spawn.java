package de.jeezycore.commands.spawn;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("spawn")) {
                if (p.hasPermission("jeezy.core.spawn.spawn")) {
                    List<Location> ls = (List<Location>) JeezyConfig.config_defaults.get("entry-spawn-point");

                    MemorySection mc = (MemorySection) JeezyConfig.config_defaults.get("spawn-settings");
                    boolean spawn_command = mc.getBoolean("spawn-command");

                    if (!spawn_command) {
                        return true;
                    }

                    World w = ls.get(0).getWorld();
                    double x = ls.get(0).getBlockX();
                    double y = ls.get(0).getBlockY();
                    double z = ls.get(0).getBlockZ();
                    float yaw = ls.get(0).getYaw();
                    float pitch = ls.get(0).getPitch();

                    p.teleport(new Location(w, x, y, z, yaw, pitch));
                } else {
                    p.sendMessage("No permission.");
                }
            }
        }
        return true;
    }
}