package de.jeezycore.commands.ranks;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.JeezySQL;
import de.jeezycore.discord.messages.grant.RealtimeGrant;
import de.jeezycore.events.inventories.grant.GrantInventory;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Wool;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class GrantRank implements CommandExecutor {
    public static Inventory grant_inv;

    JeezySQL mysql = new JeezySQL();

    UUIDChecker uc = new UUIDChecker();
    GrantInventory grantInventory = new GrantInventory();
    RealtimeGrant grant_discord = new RealtimeGrant();


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;


            if (p.hasPermission("jeezy.core.rank.grant")) {
                if (cmd.getName().equalsIgnoreCase("grant") && args.length == 0) {
                    p.sendMessage("grant <player> | grant <rank> <player>");
                    return true;
                }
                if (cmd.getName().equalsIgnoreCase("grant") && args.length == 1) {
                    grantInventory.grant_menu(p, args[0]);
                } else {
                    uc.check(args[1]);
                    ArrayStorage.grant_array_names.put(p.getUniqueId(), args[1]);
                    ArrayStorage.grant_array.put(p.getUniqueId(), UUID.fromString(UUIDChecker.uuid));
                    mysql.grantPlayerNoGui(args[0], p.getUniqueId());
                    System.out.println(UUIDChecker.uuid);
                    mysql.onGrantingPerms(p);
                    grant_discord.realtimeChatOnGranting(ArrayStorage.grant_array.get(p.getUniqueId()), ArrayStorage.grant_array_names.get(p.getUniqueId()), p.getDisplayName(), args[0]);
                    ArrayStorage.grant_array.clear();
                    ArrayStorage.grant_array_names.clear();
                }
            } else {
                p.sendMessage("No permission");
            }
        }

        return true;
    }
}