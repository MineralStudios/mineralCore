package de.jeezycore.commands.basic.msg;

import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.jeezycore.utils.ArrayStorage.msg_ignore_list;

public class Ignore implements CommandExecutor {
    UUIDChecker uuidChecker = new UUIDChecker();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ignore") && args.length == 1) {
                uuidChecker.check(args[0]);
                if (msg_ignore_list.get(p.getPlayer().getUniqueId()) != null && msg_ignore_list.get(p.getPlayer().getUniqueId()).contains(UUIDChecker.uuid)) {
                        p.sendMessage("§7You have already §cignored §7that player.");
                        return true;
                } else {
                    String oldIgnores = msg_ignore_list.get(p.getPlayer().getUniqueId());
                    msg_ignore_list.put(p.getPlayer().getUniqueId(), oldIgnores + UUIDChecker.uuid);
                    p.sendMessage("§7You §2successfully §7ignored §9" + args[0] + "§7.");
                }
            }
        }
        return true;
    }
}