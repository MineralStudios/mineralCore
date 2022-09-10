package de.jeezycore.events;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


import java.io.File;
import java.util.*;


public class JoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");

    try {
        File file = new File("C:\\Users\\Lassd\\IdeaProjects\\JeezyDevelopment\\JeezyCore\\src\\config.yml");
        FileConfiguration spawn = YamlConfiguration.loadConfiguration(file);

        List<Location> ls = (List<Location>) spawn.get("entry-spawn-point");


        World w = ls.get(0).getWorld();
        double x = ls.get(0).getBlockX();
        double y = ls.get(0).getBlockY();
        double z = ls.get(0).getBlockZ();
        float pitch = ls.get(0).getPitch();
        float yaw = ls.get(0).getYaw();
        e.getPlayer().teleport(new Location(w, x, y, z, pitch, yaw));

    } catch (Exception f) {
    f.printStackTrace();
    }

    }

}
