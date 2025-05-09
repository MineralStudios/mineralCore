package de.jeezycore.db.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

public class PlayersCache {

    private static final PlayersCache INSTANCE = new PlayersCache();
    private final Cache<String, JSONArray> cache;
    private final JSONArray playerDataArray;
    private final HashMap<UUID, String> quickNewTagsStorage = new HashMap<>();


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

    public void onPlayerTagEdit(Player p, String newTag) {
        try {
            if (getAllPlayerData() != null) {
                for (Object obj : TagsCache.getInstance().getAllTags()) {
                    JSONObject jsonOB = (JSONObject) obj;
                    if (jsonOB.optString("tagName").equalsIgnoreCase(newTag.substring(4))) {
                        quickNewTagsStorage.put(p.getUniqueId(), jsonOB.optString("tagDesign"));
                        break;
                    }

                }
                for (Object obj : getAllPlayerData()) {
                    JSONObject jsonOB = (JSONObject) obj;

                    if (jsonOB.get("playerUUID").toString().equalsIgnoreCase(p.getUniqueId().toString())) {
                        jsonOB.put("tagDesign", quickNewTagsStorage.get(p.getUniqueId()));
                        quickNewTagsStorage.remove(p.getUniqueId());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onPlayerTagRemove (Player p) {
        for (Object obj : getAllPlayerData()) {
            JSONObject jsonOB = (JSONObject) obj;

            if (jsonOB.get("playerUUID").toString().equalsIgnoreCase(p.getUniqueId().toString())) {
                jsonOB.remove("tagDesign");
            }
        }
    }


    public void savePlayerData(UUID playerTagUUID, String rankColor, String rank, String tagName, String tagDesign, String chatColor, String playTime, boolean online) {
        JSONObject playersData = new JSONObject();

        safePut(playersData,"playerUUID", playerTagUUID);
        safePut(playersData, "rankColor", rankColor);
        safePut(playersData,"rank", rank);
        safePut(playersData, "tagName", tagName);
        safePut(playersData,"tagDesign", tagDesign);
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
