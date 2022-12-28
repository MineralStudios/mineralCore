package de.jeezycore.commands.store;

import de.jeezycore.events.inventories.store.StoreInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Store implements CommandExecutor {
    StoreInventory storeInventory = new StoreInventory();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("store")) {
                storeInventory.store_menu(p);
            }
        }
        return true;
    }
}