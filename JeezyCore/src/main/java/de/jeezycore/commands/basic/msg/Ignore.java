package de.jeezycore.commands.basic.msg;

import de.jeezycore.db.SettingsSQL;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.jeezycore.utils.ArrayStorage.msg_ignore_list;

public class Ignore implements CommandExecutor {
    SettingsSQL settingsSQL = new SettingsSQL();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ignore") && args.length == 1) {
                    settingsSQL.addIgnore(p, args[0]);
            } else {
            settingsSQL.showIgnoreList(p);
            }
        }
        return true;
    }
}