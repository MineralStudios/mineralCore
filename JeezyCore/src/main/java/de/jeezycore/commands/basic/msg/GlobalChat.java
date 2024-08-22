package de.jeezycore.commands.basic.msg;

import de.jeezycore.db.SettingsSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalChat implements CommandExecutor {

    SettingsSQL settingsSQL = new SettingsSQL();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("gc") && args.length == 0) {
                settingsSQL.getSettingsData(p.getUniqueId());
                if (settingsSQL.settingsGlobalChat) {
                    settingsSQL.disableGlobalChat(p);
                    p.sendMessage("§7You successfully §cdisabled §7the global §9chat§7.");
                } else {
                    settingsSQL.enableGlobalChat(p);
                    p.sendMessage("§7You successfully §aenabled §7the global §9chat§7.");
                }
            } else {
                p.sendMessage("Usage: /pmSounds");
            }
        }
        return true;
    }
}