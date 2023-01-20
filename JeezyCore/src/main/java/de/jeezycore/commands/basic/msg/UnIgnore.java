package de.jeezycore.commands.basic.msg;

import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.jeezycore.utils.ArrayStorage.msg_ignore_list;

public class UnIgnore implements CommandExecutor {
    UUIDChecker uuidChecker = new UUIDChecker();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("unignore") && args.length == 1) {
                   uuidChecker.check(args[0]);
                    if (msg_ignore_list.get(p.getPlayer().getUniqueId()) != null && msg_ignore_list.get(p.getPlayer().getUniqueId()).contains(UUIDChecker.uuid)) {
                            String updatedIgnoreList = msg_ignore_list.get(p.getPlayer().getUniqueId()).replace(UUIDChecker.uuid, "");
                            msg_ignore_list.put(p.getPlayer().getUniqueId(), updatedIgnoreList);
                            p.sendMessage("§7You have §2successfully §7removed §9"+args[0]+" §7from the ignore list.");
                            System.out.println(msg_ignore_list.get(p.getPlayer().getUniqueId()));
                            return true;

                    } else {
                       p.sendMessage("§7You haven't §cignored §9"+args[0]+" §7yet.");
                    }
            }
        }
        return true;
    }
}