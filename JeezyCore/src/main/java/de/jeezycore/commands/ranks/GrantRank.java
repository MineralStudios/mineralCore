package de.jeezycore.commands.ranks;

import de.jeezycore.colors.ColorTranslator;
import de.jeezycore.db.JeezySQL;
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

            if (cmd.getName().equalsIgnoreCase("grant-rank") && args.length == 0) {
                p.sendMessage("Usage /grant-rank (player)");
            } else {
                if (p.hasPermission("jeezy.core.rank.grant")) {
                    grant_inv = Bukkit.createInventory(null, 27,"§8Grant Menu");
                    JeezySQL display = new JeezySQL();
                    display.displayData();

                    ArrayStorage.grant_inv_array.put(p.getPlayer().getDisplayName(), grant_inv);


                    UUIDChecker c = new UUIDChecker();
                    c.check(args[0]);

                    ArrayStorage.grant_array_names.put(p.getUniqueId(), args[0]);
                    ArrayStorage.grant_array.put(p.getUniqueId(), UUID.fromString(UUIDChecker.uuid));
                    System.out.println(ArrayStorage.grant_array);

                    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                    SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                    skullMeta.setOwner(ArrayStorage.grant_array_names.get(p.getUniqueId()));

                    List<String> player_desc = new ArrayList<String>();
                    player_desc.add(0, "§8§m-----------------------------------");
                    player_desc.add(1, "§7§lClick§7 to configurate §7§lranks §7and §7§lmore");
                    player_desc.add(2, "§8§m-----------------------------------");
                    skullMeta.setDisplayName("§b"+ArrayStorage.grant_array_names.get(p.getUniqueId()));
                    skullMeta.setLore(player_desc);
                    skull.setItemMeta(skullMeta);
                    grant_inv.setItem(4, skull);


                    if (display.rankData.size() == 0) {
                        p.openInventory(ArrayStorage.grant_inv_array.get(p.getPlayer().getDisplayName()));
                        return true;
                    } else {
                        int i = 0;
                        for (Map.Entry<String, Integer> entry : display.rankData.entrySet()) {
                            ItemStack rank = new ItemStack(Material.WOOL, 1, (short) ((int) entry.getValue()));
                            String show_color = ColorTranslator.colorTranslator.get(entry.getValue());
                            String displayName = entry.getKey();
                            ItemMeta rankMeta = rank.getItemMeta();
                            List<String> desc = new ArrayList<String>();
                            desc.add(0, "§8§m-----------------------------------");
                            desc.add(1, "§7Click to grant §l"+show_color+displayName+"§7 to " + "§b"+args[0]);
                            desc.add(2, "§8§m-----------------------------------");
                            rankMeta.setDisplayName(show_color+displayName);
                            rankMeta.setLore(desc);
                            rank.setItemMeta(rankMeta);
                            grant_inv.setItem(9+i, rank);
                            ++i;
                        }
                    }
                    p.openInventory(ArrayStorage.grant_inv_array.get(p.getPlayer().getDisplayName()));
                } else {
                    p.sendMessage("No permission");
                }
            }
        }
        return false;
    }
}