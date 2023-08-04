package de.jeezycore.commands.basic;

import de.jeezycore.config.JeezyConfig;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import java.io.File;
import java.util.List;

public class World implements CommandExecutor {

    MemorySection multiverseWorlds = (MemorySection) JeezyConfig.multiverse_defaults.get("Multiverse");
    List<String> worlds = (List<String>) multiverseWorlds.get("worlds");

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

                                worlds.add(args[1]);

                                JeezyConfig.multiverse_defaults.set("Multiverse.worlds", worlds);

                                JeezyConfig.multiverse_defaults.save(JeezyConfig.multiverse);

                                p.sendMessage("§7§l[§9§lWorlds§7] §2Successfully §7loaded §f/ §7created the world §9"+ args[1]+"§7.");
                            } else {
                                helpMessage(p);
                            }
                            break;
                        case "delete":
                            if (args.length == 2) {
                                if (!Bukkit.getServer().getWorlds().toString().contains(args[1])) {
                                    p.sendMessage("§7§l[§9§lWorlds§7] §7The world §cdoesn't §7exist.");
                                } else {

                                    worlds.remove(args[1]);

                                    JeezyConfig.multiverse_defaults.set("Multiverse.worlds", worlds);

                                    JeezyConfig.multiverse_defaults.save(JeezyConfig.multiverse);

                                    Bukkit.unloadWorld(args[1], false);

                                    FileUtils.deleteDirectory(new File(args[1]));

                                    p.sendMessage("§7§l[§9§lWorlds§7] §7You §2successfully §7deleted the world: §9"+args[1]+"§7.");
                                }
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
        for (String worldNames : worlds) {
            new WorldCreator(worldNames).createWorld();
        }
    }

    private void helpMessage(Player p) {
        p.sendMessage(new String[] {
                " §9§lWorlds §f§lHelp",
                "                                                                   ",
                " §f/§9world §fload / creates §7<world> §f- §7Loads / Creates a world.",
                " §f/§9world §fdelete §7<world> §f- §7Deletes a world that has been loaded.",
                " §f/§9world §ftp §7<world> §f- §7Teleports player to another world.",
                " §f/§9world §flist §f- §7Shows all worlds.",
                "                                                                   "
        });
    }

}
