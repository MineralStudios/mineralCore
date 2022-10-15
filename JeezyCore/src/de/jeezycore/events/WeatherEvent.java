package de.jeezycore.events;

import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.io.File;
import java.util.List;

public class WeatherEvent implements Listener {

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {

        File file = new File("C:\\Users\\Lassd\\IdeaProjects\\JeezyDevelopment\\JeezyCore\\src\\config.yml");
        FileConfiguration spawn = YamlConfiguration.loadConfiguration(file);
        MemorySection mc = (MemorySection) spawn.get("settings");
        boolean rain_b = mc.getBoolean("disable_raining");

        if (rain_b) {
            e.setCancelled(e.toWeatherState());
        }
        }
    }