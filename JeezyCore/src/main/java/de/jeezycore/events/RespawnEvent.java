package de.jeezycore.events;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RespawnEvent implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        try {

            List<Location> ls = (List<Location>) JeezyConfig.config_defaults.get("entry-spawn-point");
            MemorySection mc = (MemorySection) JeezyConfig.config_defaults.get("spawn-settings");
            boolean spawn_settings = mc.getBoolean("respawn-at-spawn");



            World w = ls.get(0).getWorld();
            double x = ls.get(0).getBlockX();
            double y = ls.get(0).getBlockY();
            double z = ls.get(0).getBlockZ();
            float pitch = ls.get(0).getPitch();
            float yaw = ls.get(0).getYaw();
            System.out.println("Here in onRespawn event");

            System.out.println(spawn_settings);
            if (!spawn_settings) {
                return;
            }
            e.setRespawnLocation(new Location(w, x, y, z, pitch, yaw));

        } catch (Exception f) {
            f.printStackTrace();
        }
    }

}
