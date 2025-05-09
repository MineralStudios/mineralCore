package de.jeezycore.db.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.jeezycore.db.TagsSQL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.UUID;
import static de.jeezycore.utils.ArrayStorage.tagPlayersMap;
import static de.jeezycore.utils.ArrayStorage.tagsCheckStatus;

public class TagsCache {

    private static final TagsCache INSTANCE = new TagsCache();
    private final Cache<String, JSONArray> cache;
    private final JSONArray tagsArray;
    private final JSONArray tagsPlayerArray;
    private final TagsSQL tagsSQL = new TagsSQL();


    private TagsCache() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(100)
                .build();
        this.tagsArray = new JSONArray();
        this.tagsPlayerArray = new JSONArray();
    }

    public static TagsCache getInstance() {
        return INSTANCE;
    }


    public void reloadAllTagsNow() {
        tagsArray.clear();
        tagsSQL.getData();
        cache.put("tags", new JSONArray(tagsArray.toString()));
    }

    public void saveTag(String tagName, String tagCategories, String tagDesign) {
        JSONObject tag = new JSONObject();
        tag.put("tagName", tagName);
        tag.put("tagCategories", tagCategories);
        tag.put("tagDesign", tagDesign);
        tagsArray.put(tag);
    }

    public void savePlayerTags(UUID playerTagUUID, String playerTag, String playerTagCategory, String playerTagDesign) {
        JSONObject tagsPlayer = new JSONObject();
        tagsPlayer.put("tagName", playerTag);
        tagsPlayer.put("tagCategories", playerTagCategory);
        tagsPlayer.put("tagDesign", playerTagDesign);
        tagPlayersMap.put(playerTagUUID, tagsPlayer);
        tagsPlayerArray.put(tagPlayersMap);
    }

    public void saveAllTags() {
        cache.put("tags", new JSONArray(tagsArray.toString()));
    }

    public void saveAllPlayerTags() {
        cache.put("playerTags", new JSONArray(tagsPlayerArray.toString()));
    }

    public JSONArray getAllTags() {
        JSONArray cached = cache.getIfPresent("tags");
        if (cached != null) {
            return cached;
        } else {
            System.out.println("❌ No tags found in cache.");
            return null;
        }
    }

    public JSONArray getAllPlayerTags() {
        JSONArray cached = cache.getIfPresent("playerTags");
        if (cached != null) {
            return cached;
        } else {
            System.out.println("❌ No playerTags found in cache.");
            return null;
        }
    }

}
