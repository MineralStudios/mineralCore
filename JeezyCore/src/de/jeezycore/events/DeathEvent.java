package de.jeezycore.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.File;
import java.util.List;

public class DeathEvent implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        try {

            File file = new File("C:\\Users\\Lassd\\IdeaProjects\\JeezyDevelopment\\JeezyCore\\src\\config.yml");
            FileConfiguration spawn = YamlConfiguration.loadConfiguration(file);

            File file2 = new File("C:\\Users\\Lassd\\IdeaProjects\\JeezyDevelopment\\JeezyCore\\src\\messages.yml");
            FileConfiguration msg = YamlConfiguration.loadConfiguration(file2);

            List<Location> ls = (List<Location>) spawn.get("entry-spawn-point");
            MemorySection mc = (MemorySection) spawn.get("spawn-settings");

            MemorySection mc2 = (MemorySection) msg.get("messages");

            boolean spawn_settings = mc.getBoolean("respawn-after-death-at-spawn");

            boolean get_death_msg_b = mc2.getBoolean("disable-death-msg");

            if (get_death_msg_b) {
                e.setDeathMessage("");
            }

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
            e.getEntity().teleport(new Location(w, x, y, z, pitch, yaw));

        } catch (Exception f) {
            f.printStackTrace();
        }
    }

}
