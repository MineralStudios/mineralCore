package de.jeezycore.db.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import java.util.UUID;

public class PlayersCache {

    private static final PlayersCache INSTANCE = new PlayersCache();
    private final Cache<String, JSONArray> cache;
    private final JSONArray playerDataArray;


    private PlayersCache() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(100)
                .build();
        this.playerDataArray = new JSONArray();
    }

    public static PlayersCache getInstance() {
        return INSTANCE;
    }

    private JSONObject safePut(JSONObject jsonObject, String key, Object value) {
        if (value != null) {
            jsonObject.put(key, value);
        }
        return jsonObject;
    }


    public void savePlayerData(UUID playerTagUUID, String rankColor, String rank, String tag, String chatColor, String playTime, boolean online) {
        JSONObject playersData = new JSONObject();

        safePut(playersData,"playerUUID", playerTagUUID);
        safePut(playersData, "rankColor", rankColor);
        safePut(playersData,"rank", rank);
        safePut(playersData,"tag", tag);
        safePut(playersData,"chatColor", chatColor);
        safePut(playersData,"playTime", playTime);
        safePut(playersData,"online", online);
        playerDataArray.put(playersData);
    }

    public void saveAllPlayerData() {
        cache.put("playersData", new JSONArray(playerDataArray.toString()));
    }

    public JSONArray getAllPlayerData() {
        JSONArray cached = cache.getIfPresent("playersData");
        if (cached != null) {
            return cached;
        } else {
            System.out.println("❌ No playerData found in cache.");
            return null;
        }
    }
}
