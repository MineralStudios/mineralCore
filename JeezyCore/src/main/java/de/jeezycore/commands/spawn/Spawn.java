package de.jeezycore.commands.spawn;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        MemorySection spawn_settings = (MemorySection) JeezyConfig.config_defaults.get("spawn-settings");
        MemorySection spawnPoint = (MemorySection) JeezyConfig.config_defaults.get("entry-spawn-point");

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("spawn")) {
                if (p.hasPermission("jeezy.core.spawn.spawn")) {
                    boolean spawn_command = spawn_settings.getBoolean("spawn-command");
                    if (!spawn_command) {
                        return true;
                    }

                    World w = Bukkit.getServer().getWorld(spawnPoint.getString("world"));
                    double x = Double.parseDouble(spawnPoint.getString("x"));
                    double y = Double.parseDouble(spawnPoint.getString("y"));
                    double z = Double.parseDouble(spawnPoint.getString("z"));
                    float yaw = Float.parseFloat(spawnPoint.getString("yaw"));
                    float pitch = Float.parseFloat(spawnPoint.getString("pitch"));

                    p.teleport(new Location(w, x, y, z, yaw, pitch));
                } else {
                    p.sendMessage("No permission.");
                }
            }
        }
        return true;
    }
}