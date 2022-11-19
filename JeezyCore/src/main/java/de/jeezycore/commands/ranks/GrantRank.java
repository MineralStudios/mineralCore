package de.jeezycore.commands.ranks;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.JeezySQL;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GrantRank implements CommandExecutor {
    public static Inventory grant_inv;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {



        if(sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("grant") && args.length == 0) {
                p.sendMessage("Usage /grant <player>");
            } else {
                if (p.hasPermission("jeezy.core.rank.grant")) {
                    GrantInventory grantInventory = new GrantInventory();
                    grantInventory.grant_menu(p, args[0]);
                } else {
                    p.sendMessage("No permission");
                }
            }
        }
        return true;
    }
}