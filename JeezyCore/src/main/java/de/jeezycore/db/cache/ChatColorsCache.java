package de.jeezycore.db.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.jeezycore.db.ChatColorSQL;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import java.util.UUID;

public class ChatColorsCache {

    private static final ChatColorsCache INSTANCE = new ChatColorsCache();
    private final Cache<String, JSONArray> cache;
    private final JSONArray chatColorsArray;
    private final JSONArray chatColorsPlayerArray;
    private final ChatColorSQL chatColorSQL = new ChatColorSQL();

    private ChatColorsCache() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(100)
                .build();
        this.chatColorsArray = new JSONArray();
        this.chatColorsPlayerArray = new JSONArray();
    }

    public static ChatColorsCache getInstance() {
        return INSTANCE;
    }

    public void saveChatColors(String colorName, String color, String colorRGB) {
        JSONObject chatColors = new JSONObject();
        chatColors.put("colorName", colorName);
        chatColors.put("color", color);
        chatColors.put("colorRGB", colorRGB);
        chatColorsArray.put(chatColors);
    }


    public void saveChatColorsPlayers(UUID playerChatColorsUUID, String currentChatColorName) {
        JSONObject chatColorsPlayer = new JSONObject();
        chatColorsPlayer.put("playerUUID", playerChatColorsUUID);
        chatColorsPlayer.put("currentChatColorName", currentChatColorName);
        chatColorsPlayerArray.put(chatColorsPlayer);
    }


    public void saveAllChatColors() {
        cache.put("chatColors", new JSONArray(chatColorsArray.toString()));
    }

    public void saveAllChatColorsPlayers() {
        cache.put("chatColorsPlayers", new JSONArray(chatColorsPlayerArray.toString()));
    }

    public JSONArray getAllChatColors() {
        JSONArray cached = cache.getIfPresent("chatColors");
        if (cached != null) {
            return cached;
        } else {
            System.out.println("❌ No chatColors found in cache.");
            return null;
        }
    }

    public JSONArray getAllChatColorsPlayers() {
        JSONArray cached = cache.getIfPresent("chatColorsPlayers");
        if (cached != null) {
            return cached;
        } else {
            System.out.println("❌ No chatColors for players found in cache.");
            return null;
        }
    }

    public void reloadChatColorsPlayersNow() {
        chatColorsPlayerArray.clear();
        chatColorSQL.getChatColorsPlayersData();
        cache.put("chatColorsPlayers", new JSONArray(chatColorsPlayerArray.toString()));
    }
}
