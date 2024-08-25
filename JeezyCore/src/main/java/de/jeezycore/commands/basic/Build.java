package de.jeezycore.commands.basic;

import de.jeezycore.utils.PermissionHandler;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.jeezycore.utils.ArrayStorage.allBuilders;
import static de.jeezycore.utils.ArrayStorage.inBuildingMode;

public class Build implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        PermissionHandler permissionHandler = new PermissionHandler();

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (allBuilders.contains(p.getPlayer().getUniqueId()) || p.getPlayer().hasPermission("mineral.build.*")) {
                if (cmd.getName().equalsIgnoreCase("build") && args.length == 0) {
                    helpMessage(p);
                    return true;
                }
                try {
                    switch (args[0]) {
                        case "on":
                            p.getPlayer().setGameMode(GameMode.CREATIVE);
                            permissionHandler.addPermsPerPlayerBuild(p, "worldedit.*");
                            permissionHandler.addPermsPerPlayerBuild(p, "multiverse.*");
                            p.getPlayer().sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §fBuilding mode §2enabled§f.");
                            inBuildingMode.add(p.getPlayer().getUniqueId());
                            break;
                        case "off":
                            p.getPlayer().setGameMode(GameMode.SURVIVAL);
                            permissionHandler.removePermsPerPlayerBuild(p, "worldedit.*");
                            permissionHandler.removePermsPerPlayerBuild(p, "multiverse.*");
                            p.getPlayer().sendMessage("§7§l[§9§lMINE§f§lRAL§7§l] §fBuilding mode §cdisabled§f.");
                            inBuildingMode.remove(p.getPlayer().getUniqueId());
                            break;
                        default:
                            helpMessage(p);
                    }
                } catch (Exception e) {
                    helpMessage(p);
                }
            } else {
                p.getPlayer().sendMessage("§cNo permission!");
            }
        }
        return true;
    }


public void helpMessage(Player p) {
    p.getPlayer().sendMessage(new String[]{
            "                                                                       ",
            " §9§lBuilder §f§lHelp",
            "                                                                       ",
            " §f/§9build §fon §f- §7Gives you all the perms you need and gamemode 1.",
            " §f/§9build §foff §f- §7Takes all the perms away and sets you back to gamemode 0.",
            "                                                                       "
    });
}
}