package de.jeezycore.commands.basic.multiverse;

import de.jeezycore.config.JeezyConfig;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
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
                    if (cmd.getName().equalsIgnoreCase("world") && args.length == 0) {
                        helpMessageWorldCommands(p);
                        return true;
                    }
                    switch (args[0]) {
                        case "create":
                            if (args.length == 4) {
                                if (!FileUtils.getFile(new File(args[1])).exists()) {
                                    WorldType worldType;

                                    if (args[3].equalsIgnoreCase("normal")) {
                                        worldType = WorldType.NORMAL;
                                    } else if (args[3].equalsIgnoreCase("flat")) {
                                        worldType = WorldType.FLAT;
                                    } else {
                                        worldType = WorldType.NORMAL;
                                    }

                                    switch (args[2]) {
                                        case "normal":
                                            new WorldCreator(args[1]).environment(org.bukkit.World.Environment.NORMAL).type(worldType).createWorld();
                                            break;
                                        case "nether":
                                            new WorldCreator(args[1]).environment(org.bukkit.World.Environment.NETHER).type(worldType).createWorld();
                                            break;
                                        case "the_end":
                                            new WorldCreator(args[1]).environment(org.bukkit.World.Environment.THE_END).type(worldType).createWorld();
                                            break;
                                        case "void":
                                            new WorldCreator(args[1]).environment(org.bukkit.World.Environment.NORMAL).type(worldType).generator(new EmptyChunkGenerator()).createWorld();
                                            break;
                                        default:
                                            helpMessageEnvironmentCommands(p);
                                            return true;
                                    }

                                    worlds.add(args[1]);

                                    JeezyConfig.multiverse_defaults.set("Multiverse.worlds", worlds);

                                    JeezyConfig.multiverse_defaults.save(JeezyConfig.multiverse);

                                    p.sendMessage("§7[§9§lWorlds§7] §2Successfully §7created the world §9"+ args[1]+"§7.");
                                } else {
                                    p.sendMessage("§7[§9§lWorlds§7] §7The world: §9"+args[1]+" §calready §7exist.");
                                }
                            } else {
                                helpMessageEnvironmentCommands(p);
                            }
                            break;
                        case "load":
                            if (args.length == 2) {
                                if (FileUtils.getFile(new File(args[1])).exists()) {
                                    new WorldCreator(args[1]).createWorld();

                                    worlds.add(args[1]);

                                    JeezyConfig.multiverse_defaults.set("Multiverse.worlds", worlds);

                                    JeezyConfig.multiverse_defaults.save(JeezyConfig.multiverse);

                                    p.sendMessage("§7[§9§lWorlds§7] §2Successfully §7loaded the world §9"+ args[1]+"§7.");
                                } else {
                                    p.sendMessage("§7[§9§lWorlds§7] §7The world §cdoesn't §7exist.");
                                }

                            } else {
                                helpMessageWorldCommands(p);
                            }
                            break;
                        case "delete":
                            if (args.length == 2) {
                                if (Bukkit.getServer().getWorlds().toString().contains(args[1])) {
                                    worlds.remove(args[1]);

                                    JeezyConfig.multiverse_defaults.set("Multiverse.worlds", worlds);

                                    JeezyConfig.multiverse_defaults.save(JeezyConfig.multiverse);

                                    Bukkit.unloadWorld(args[1], false);

                                    FileUtils.deleteDirectory(new File(args[1]));

                                    p.sendMessage("§7[§9§lWorlds§7] §7You §2successfully §7deleted the world: §9"+args[1]+"§7.");
                                } else {
                                    p.sendMessage("§7[§9§lWorlds§7] §7The world §cdoesn't §7exist.");
                                }
                            } else {
                                helpMessageWorldCommands(p);
                            }
                            break;
                        case "tp":
                            if (args.length == 2) {
                                if (Bukkit.getServer().getWorlds().toString().contains(args[1])) {

                                    org.bukkit.World w = Bukkit.getServer().getWorld(args[1]);
                                    Location location = w.getSpawnLocation();

                                    p.teleport(new Location(w, location.getX(), location.getY(), location.getZ()));

                                } else {
                                    p.sendMessage("§7[§9§lWorlds§7] §7The world §cdoesn't §7exist.");
                                }
                            } else {
                                helpMessageWorldCommands(p);
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
                        default:
                            helpMessageWorldCommands(p);
                            break;
                    }
                } catch (Exception e) {
                    helpMessageWorldCommands(p);
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

    private void helpMessageWorldCommands(Player p) {
        p.sendMessage(new String[] {
                "                                                                   ",
                " §9§lWorlds §f§lHelp",
                "                                                                   ",
                " §f/§9world §fcreate §7<world> <environment> <type> §f- §7Creates a world.",
                " §f/§9world §fload §7<world> §f- §7Loads a world.",
                " §f/§9world §fdelete §7<world> §f- §7Deletes a world that has been loaded.",
                " §f/§9world §ftp §7<world> §f- §7Teleports player to another world.",
                " §f/§9world §flist §f- §7Shows all worlds.",
                "                                                                   "
        });
    }

    private void helpMessageEnvironmentCommands(Player p) {
        p.sendMessage(new String[] {
                "                                                                   ",
                " §9§lAvailable §f§lEnvironments",
                "                                                                   ",
                " §f/§9world §fcreate §7<world> <§2normal§7> <§2normal§f/§2flat§7> §f- §7Creates a §2normal §7world.",
                " §f/§9world §fcreate §7<world> <§2nether§7> <§2normal§f/§2flat§7> §f- §7Creates a §2nether §7world.",
                " §f/§9world §fcreate §7<world> <§2the_end§7> <§2normal§f/§2flat§7> §f- §7Creates a §2the_end §7world.",
                " §f/§9world §fcreate §7<world> <§2void§7> <§2normal§f/§2flat§7> §f- §7Creates a §2void §7world.",
                "                                                                   "
        });
    }
}