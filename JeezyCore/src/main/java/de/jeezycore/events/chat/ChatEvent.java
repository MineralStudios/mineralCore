package de.jeezycore.events.chat;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.ChatColorSQL;
import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.TagsSQL;
import de.jeezycore.discord.messages.realtime.RealtimeChat;
import de.jeezycore.utils.NameMC;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import static de.jeezycore.db.TagsSQL.tag_in_chat;
import static de.jeezycore.utils.ArrayStorage.playerNickedList;

public class ChatEvent implements Listener {

    MemorySection cf = (MemorySection) JeezyConfig.config_defaults.get("chat");
    TagsSQL tagsSQL = new TagsSQL();
    RanksSQL display = new RanksSQL();

    RealtimeChat rmc = new RealtimeChat();

    BanChat banChat = new BanChat();

    IgnoreChat ignoreChat = new IgnoreChat();
    StaffChat staffChat = new StaffChat();

    ChatColorSQL chatColorSQL = new ChatColorSQL();

    AntiSpam antiSpam = new AntiSpam();

    GlobalChat globalChat = new GlobalChat();

    NameMC nameMC = new NameMC();

    String chat_format_rep;

    String setNameMcTag;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        globalChat.checkGlobalChat(e);
        antiSpam.AntiSpamChat(e);
        staffChat.chat(e);
        display.getPlayerInformation(e.getPlayer().getUniqueId());
        String sql = "SELECT * FROM ranks WHERE rankName = '"+ RanksSQL.rankNameInformation +"'";
        display.displayChatRank(sql);
        tagsSQL.tagChat(e.getPlayer());
        banChat.onPlayerChatBan(e);
        ignoreChat.PlayerIgnoreChat(e);
        chatColorSQL.getPlayerChatName(e.getPlayer());
        setNameMcTag = nameMC.checkIfAlreadyVoted(e.getPlayer()) ? "§7[§3Mineral§7]§f " : "";

        if (playerNickedList.containsKey(e.getPlayer().getUniqueId())) {
            chat_format_rep = cf.getString("chat_format").replace("[rank]", "").replace("[player]", e.getPlayer().getDisplayName()).replace("[msg]", e.getMessage()).replace("[tag]", "");
            e.setFormat(chat_format_rep.replace("%", "%%").trim());
            rmc.realtimeMcChat( e.getPlayer().getDisplayName()+": "+e.getMessage());
            return;
        }

        if (RanksSQL.rankNameInformation == null || display.rank == null) {
                 chat_format_rep = cf.getString("chat_format").replace("[rank]", display.rank).replace("[player]", setNameMcTag+ChatColorSQL.currentChatColor+e.getPlayer().getDisplayName()).replace("[msg]", e.getMessage()).replace("[tag]", tag_in_chat);
                rmc.realtimeMcChat( e.getPlayer().getDisplayName()+": "+e.getMessage());
            } else {
                chat_format_rep = cf.getString("chat_format").replace("[rank]", "§7["+display.rankColor+""+display.rank+"§7]§f").replace("[player]", setNameMcTag+ChatColorSQL.currentChatColor+e.getPlayer().getDisplayName()).replace("[msg]", e.getMessage()).replace("[tag]", tag_in_chat);
                rmc.realtimeMcChat("["+display.rank+"]"+" "+e.getPlayer().getDisplayName()+": "+e.getMessage());
            }
            e.setFormat(chat_format_rep.replace("%", "%%").trim());
    }
}