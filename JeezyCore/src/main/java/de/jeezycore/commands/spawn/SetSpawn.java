package de.jeezycore.commands.spawn;

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

            File file = new File("C:\\Users\\Lassd\\IdeaProjects\\JeezyDevelopment\\JeezyCore\\src\\config.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (!file.exists()) {
                file.createNewFile(); //This needs a try catch
            }

            Location worldObject = p.getLocation();

            List<Location> locations = new ArrayList<>(Arrays.asList(worldObject));
            config.set("entry-spawn-point", locations);

            config.set("world", p.getPlayer().getLocation().getWorld().getName());
            config.set("x", p.getPlayer().getLocation().getBlockX());
            config.set("y", p.getPlayer().getLocation().getBlockY());
            config.set("z", p.getPlayer().getLocation().getBlockZ());
            config.set("pitch", p.getPlayer().getLocation().getPitch());
            config.set("yaw", p.getPlayer().getLocation().getYaw());

            //To remove something do 'config.set("Whatever.Path-Here", null)'
            config.save(file); //This needs a try catch

        } catch (Exception f) {
            f.printStackTrace();
        }

            }

        }
        return false;
    }
}
