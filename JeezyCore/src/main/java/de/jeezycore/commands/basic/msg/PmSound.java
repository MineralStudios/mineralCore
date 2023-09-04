package de.jeezycore.commands.basic.msg;

import de.jeezycore.db.SettingsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PmSound implements CommandExecutor {

    SettingsSQL settingsSQL = new SettingsSQL();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("pmSound") && args.length == 0) {
                settingsSQL.getSettingsData(p.getUniqueId());
            if (settingsSQL.settingsPmSound) {
                settingsSQL.disablePmSound(p);
                p.sendMessage("§7You successfully §cdisabled §7private message §9sounds§7.");
            } else {
                settingsSQL.enablePmSound(p);
                p.sendMessage("§7You successfully §aenabled §7private message §9sounds§7.");
            }
            } else {
                p.sendMessage("Usage: /pmSounds");
            }
        }
        return true;
    }
}