package de.jeezycore.commands.basic;

import de.jeezycore.db.SyncSQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sync implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        SyncSQL syncSQL = new SyncSQL();

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("sync") && args.length == 0) {
                syncSQL.createCode(p);
                return true;
            }

            try {
                switch (args[0]) {
                    case "code":
                        if (args.length == 1) {
                            syncSQL.showCode(p);
                        }
                        break;
                    case "unlink":
                        if (args.length == 1) {
                            syncSQL.unLink(p);
                        }
                        break;
                    default:
                        syncHelp(p);
                        break;
                }
            } catch (Exception e) {
                syncHelp(p);
            }
        }
        return true;
    }

    private void syncHelp(Player p) {
        p.getPlayer().sendMessage(new String[]{
                "                                                                       ",
                " §9§lSync §f§lHelp",
                "                                                                       ",
                " §f/§9sync §f- §7Links your minecraft account with discord.",
                " §f/§9sync §fcode §f- §7Shows your code again.",
                " §f/§9sync §funlink §f- §7Unlinks your minecraft account from discord.",
                "                                                                       "
        });
    }
}