package de.jeezycore.commands.ranks;

import de.jeezycore.db.ChatColorSQL;
import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.TagsSQL;
import de.jeezycore.discord.messages.grant.RealtimeGrant;
import de.jeezycore.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnGrantRank implements CommandExecutor {

    UUIDChecker uc = new UUIDChecker();
    RanksSQL grant = new RanksSQL();
    TagsSQL tagsSQL = new TagsSQL();
    ChatColorSQL chatColorSQL = new ChatColorSQL();
    RealtimeGrant discord = new RealtimeGrant();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("jeezy.core.rank.ungrant")) {
                if (cmd.getName().equalsIgnoreCase("ungrant") && args.length == 1) {
                    return true;
                } else {
                    p.sendMessage("Usage: /ungrant <playerName>");
                }
            }
        } else {
        if (cmd.getName().equalsIgnoreCase("ungrant") && args.length == 1) {
            uc.check(args[0]);
            grant.removeRankConsole(sender, UUIDChecker.uuidName, UUID.fromString(UUIDChecker.uuid));
            tagsSQL.resetTagOnUnGrantingRank();
            chatColorSQL.resetChatColorsOnUnGrantingRank();
            discord.realtimeChatOnUnGranting(UUID.fromString(UUIDChecker.uuid), UUIDChecker.uuidName, "Console");
        } else {
         sender.sendMessage("ungrant <playerName>");
        }

        }

        return true;
    }
}
