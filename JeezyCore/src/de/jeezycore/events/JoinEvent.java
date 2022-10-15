package de.jeezycore.events;


import de.jeezycore.db.JeezySQL;
import de.jeezycore.utils.PermissionHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
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
        JeezySQL givePermsOnJoin = new JeezySQL();
        givePermsOnJoin.onJoinPerms(e.getPlayer().getUniqueId());

        File file = new File("C:\\Users\\Lassd\\IdeaProjects\\JeezyDevelopment\\JeezyCore\\src\\config.yml");
        FileConfiguration spawn = YamlConfiguration.loadConfiguration(file);

        List<Location> ls = (List<Location>) spawn.get("entry-spawn-point");
        MemorySection mc = (MemorySection) spawn.get("spawn-settings");
        boolean spawnOnSpownpointOnJoin = mc.getBoolean("spawn-at-spawnpoint-on-join");

        World w = ls.get(0).getWorld();
        double x = ls.get(0).getBlockX();
        double y = ls.get(0).getBlockY();
        double z = ls.get(0).getBlockZ();
        float pitch = ls.get(0).getPitch();
        float yaw = ls.get(0).getYaw();

        if (!spawnOnSpownpointOnJoin) return;
        e.getPlayer().teleport(new Location(w, x, y, z, yaw, pitch));

    } catch (Exception f) {
    f.printStackTrace();
    }

    }

}
