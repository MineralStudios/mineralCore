package de.jeezycore.events.chat;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.db.ChatColorSQL;
import de.jeezycore.db.RanksSQL;
import de.jeezycore.db.TagsSQL;
import de.jeezycore.db.services.PlayersService;
import de.jeezycore.discord.messages.realtime.RealtimeChat;
import de.jeezycore.utils.NameMC;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

    private final PlayersService playersService = new PlayersService();


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        /*
        globalChat.checkGlobalChat(e);
        antiSpam.AntiSpamChat(e);
        staffChat.chat(e);
        banChat.onPlayerChatBan(e);
        ignoreChat.PlayerIgnoreChat(e);
         */
       // setNameMcTag = nameMC.checkIfAlreadyVoted(e.getPlayer()) ? "§7[§3Mineral§7]§f " : "";

                try {
                    JSONParser jsParser = new JSONParser();
                    JSONArray jsonA = (JSONArray) jsParser.parse(playersService.getAllPlayerData().toString());

                    if (playerNickedList.containsKey(e.getPlayer().getUniqueId())) {
                        chat_format_rep = cf.getString("chat_format")
                                .replace("[rank]", "")
                                .replace("[player]", e.getPlayer().getDisplayName())
                                .replace("[msg]", e.getMessage())
                                .replace("[tag]", "");
                    } else {
                        for (Object obj : jsonA) {
                            JSONObject jsonOB = (JSONObject) obj;

                            String chatColor = jsonOB.get("chatColor") != null ? jsonOB.get("chatColor").toString() : "";
                            String tag = jsonOB.get("tag") != null ? " "+ jsonOB.get("tag").toString() : "";
                            String rankColor = jsonOB.get("rankColor") != null ? (String) jsonOB.get("rankColor") : "";
                            String rank = jsonOB.get("rank") != null ? "§7["+rankColor+jsonOB.get("rank") + "§7]§f" : "";

                            if (jsonOB.get("playerUUID").toString().equalsIgnoreCase(e.getPlayer().getUniqueId().toString())) {
                                chat_format_rep = cf.getString("chat_format")
                                        .replace("[rank]", rank)
                                        .replace("[player]", chatColor+e.getPlayer().getDisplayName())
                                        .replace("[msg]", e.getMessage())
                                        .replace("[tag]", tag);
                            }
                        }
                         e.setFormat(chat_format_rep.replace("%", "%%").trim());
                        rmc.realtimeMcChat( e.getPlayer().getDisplayName()+": "+e.getMessage());
                    }
                } catch (Exception ex) {
                }
    }
}