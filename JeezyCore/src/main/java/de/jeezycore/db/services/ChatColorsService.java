package de.jeezycore.db.services;

import de.jeezycore.db.ChatColorSQL;
import de.jeezycore.db.cache.ChatColorsCache;
import org.json.JSONArray;
import java.util.concurrent.CompletableFuture;

public class ChatColorsService {

    private final ChatColorSQL chatColorSQL = new ChatColorSQL();
    private final ChatColorsCache chatColorsCache = ChatColorsCache.getInstance();

    public void load() {
        CompletableFuture.runAsync(() -> {
            chatColorSQL.getChatColorsData();
            chatColorSQL.getChatColorsPlayersData();
        });
    }

    public JSONArray getAllChatColors() {
        return chatColorsCache.getAllChatColors();
    }

    public JSONArray getAllChatColorsPlayers() {
        return chatColorsCache.getAllChatColorsPlayers();
    }
}
