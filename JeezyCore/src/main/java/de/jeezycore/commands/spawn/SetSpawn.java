package de.jeezycore.commands.spawn;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.spawn.setup")) {
            if (cmd.getName().equalsIgnoreCase("spawnSet") && args.length == 0) {
                    try {
                        Location worldObject = p.getLocation();

                        String x = "entry-spawn-point.x";
                        String y = "entry-spawn-point.y";
                        String z = "entry-spawn-point.z";
                        String pitch = "entry-spawn-point.pitch";
                        String yaw = "entry-spawn-point.yaw";
                        String world = "entry-spawn-point.world";

                        JeezyConfig.config_defaults.set(x, worldObject.getBlockX());
                        JeezyConfig.config_defaults.set(y, worldObject.getBlockY());
                        JeezyConfig.config_defaults.set(z, worldObject.getBlockZ());
                        JeezyConfig.config_defaults.set(pitch, worldObject.getPitch());
                        JeezyConfig.config_defaults.set(yaw, worldObject.getYaw());
                        JeezyConfig.config_defaults.set(world, worldObject.getWorld().getName());

                        JeezyConfig.config_defaults.save(JeezyConfig.config);

                        p.sendMessage("§2§lYou successfully setuped the spawn.");

                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                } else {
                p.sendMessage("Usage /spawnSet");
                }
            } else {
                p.sendMessage("No permission.");
            }
        }
        return true;
    }
}