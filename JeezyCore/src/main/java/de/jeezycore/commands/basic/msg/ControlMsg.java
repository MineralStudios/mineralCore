package de.jeezycore.commands.basic.msg;

import de.jeezycore.db.SettingsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ControlMsg implements CommandExecutor {

    SettingsSQL settingsSQL = new SettingsSQL();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("tpm") && args.length == 0) {
            settingsSQL.getSettingsData(p.getUniqueId());
            if (settingsSQL.settingsMsg) {
                settingsSQL.disableMsg(p);
                p.sendMessage("§7You successfully §cdisabled §7private messages.");
            } else {
                settingsSQL.enableMsg(p);
                p.sendMessage("§7You successfully §2enabled §7private messages.");
            }
        } else {
            p.sendMessage("Usage: /tpm");
        }
        }
        return true;
    }
}