package de.jeezycore.commands.basic.msg;

import de.jeezycore.db.SettingsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class ControlMsg implements CommandExecutor {

    ArrayList<UUID> users = new ArrayList<UUID>();
    SettingsSQL settingsSQL = new SettingsSQL();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("tpm") && args.length == 0) {

            if (users.contains(p.getUniqueId())) {
                users.remove(p.getUniqueId());
                settingsSQL.disableMsg(p);
                p.sendMessage("§7You successfully §cdisabled §7privat messages.");
            } else {
                users.add(p.getUniqueId());
                settingsSQL.enableMsg(p);
                p.sendMessage("§7You successfully §2enabled §7privat messages.");
            }
        } else {
            p.sendMessage("Usage: /tpm");
        }
        }
        return true;
    }
}