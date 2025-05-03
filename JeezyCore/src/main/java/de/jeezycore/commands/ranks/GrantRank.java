package de.jeezycore.commands.ranks;

import de.jeezycore.db.RanksSQL;
import de.jeezycore.discord.messages.grant.RealtimeGrant;
import de.jeezycore.events.inventories.grant.GrantInventory;
import de.jeezycore.utils.PermissionHandler;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class GrantRank implements CommandExecutor {

    RanksSQL mysql = new RanksSQL();

    UUIDChecker uc = new UUIDChecker();
    GrantInventory grantInventory = new GrantInventory();
    RealtimeGrant grant_discord = new RealtimeGrant();

    PermissionHandler permissionHandler = new PermissionHandler();


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;


            if (p.hasPermission("jeezy.core.rank.grant")) {
                if (cmd.getName().equalsIgnoreCase("grant") && args.length == 1) {
                    grantInventory.grant_menu(p, args[0]);
                } else if (cmd.getName().equalsIgnoreCase("grant") && args.length == 3) {
                    uc.check(args[1]);
                    mysql.grantPlayerConsole(sender, args[0], UUID.fromString(UUIDChecker.uuid));
                    mysql.setRankDuration(sender, UUIDChecker.uuidName, args[2], UUID.fromString(UUIDChecker.uuid));
                    mysql.colorPerms(args[0]);
                    grant_discord.realtimeChatOnGranting(UUID.fromString(UUIDChecker.uuid), UUIDChecker.uuidName, p.getDisplayName(), args[0]);
                } else {
                    p.sendMessage("Usage: /grant <playerName>(GUI) | /grant <rankName> <player> <time>");
                }

            } else {
                p.sendMessage("No permission");
            }
        } else {
             if (cmd.getName().equalsIgnoreCase("grant") && args.length == 3) {
                 uc.check(args[1]);
                 mysql.grantPlayerConsole(sender, args[0], UUID.fromString(UUIDChecker.uuid));
                 mysql.setRankDuration(sender, UUIDChecker.uuidName, args[2], UUID.fromString(UUIDChecker.uuid));
                 mysql.colorPerms(args[0]);
                 grant_discord.realtimeChatOnGranting(UUID.fromString(UUIDChecker.uuid), UUIDChecker.uuidName, "Console", args[0]);
             } else {
                 sender.sendMessage("Usage: /grant <rankName> <player> <time>");
             }
        }
        return true;
    }
}