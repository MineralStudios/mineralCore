package de.jeezycore.events;

import de.jeezycore.config.JeezyConfig;
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

        MemorySection mc = (MemorySection) JeezyConfig.config_defaults.get("settings");
        boolean rain_b = mc.getBoolean("disable_raining");

        if (rain_b) {
            e.setCancelled(e.toWeatherState());
        }
        }
    }