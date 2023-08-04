package de.jeezycore.commands.basic;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World implements CommandExecutor {

    MemorySection mc = (MemorySection) JeezyConfig.multiverse_defaults.get("multiverse");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.hasPermission("jeezy.core.world.tp") || p.hasPermission("jeezy.core.world.*")) {
                try {
                    switch (args[0]) {
                        case "load":
                            if (args.length == 2) {
                                new WorldCreator(args[1]).createWorld();
                                p.sendMessage("§2Successfully §7loaded §f/ §7created the world §9"+ args[1]+"§7.");


                                /*
                                Location worldObject = p.getLocation();

                                List<Location> locations = new ArrayList<>(Arrays.asList(worldObject));
                                JeezyConfig.config_defaults.set("entry-spawn-point", locations);
                                JeezyConfig.config_defaults.save(JeezyConfig.config);
                                 */

                            } else {
                                helpMessage(p);
                            }
                            break;
                        case "tp":
                            if (args.length == 2) {

                                org.bukkit.World w = Bukkit.getServer().getWorld(args[1]);
                                Location location = w.getSpawnLocation();

                                p.teleport(new Location(w, location.getX(), location.getY(), location.getZ()));
                            } else {
                                helpMessage(p);
                            }
                            break;
                        case "list":
                            p.sendMessage(new String[] {
                                    "                                                                   ",
                                    "  §9§lWorlds §f§lList",
                                    "                                                                   "
                            });
                            for (org.bukkit.World w : Bukkit.getServer().getWorlds()) {
                                p.sendMessage("   §9"+w.getName());
                                p.sendMessage("                                         ");
                            }
                            break;
                    }
                } catch (Exception e) {
                    helpMessage(p);
                    e.printStackTrace();
                }
            }

        }
        return true;
    }

    public void loadWorldsOnStartup() {
        new WorldCreator("StrafeHub").createWorld();
    }

    private void helpMessage(Player p) {
        p.sendMessage(new String[] {
                " §9§lWorlds §f§lHelp",
                "                                                                   ",
                " §f/§9world §fload §7<world> §f- §7Loads a world.",
                " §f/§9world §ftp §7<world> §f- §7Teleports player to another world.",
                " §f/§9world §flist §f- §7Shows all worlds.",
                "                                                                   "
        });
    }

}
