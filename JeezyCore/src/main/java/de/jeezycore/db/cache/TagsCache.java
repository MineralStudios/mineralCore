package de.jeezycore.db.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.jeezycore.colors.Color;
import de.jeezycore.db.TagsSQL;
import org.json.JSONArray;
import org.json.JSONObject;

public class TagsCache {

    private static final TagsCache INSTANCE = new TagsCache();
    private final Cache<String, JSONArray> cache;
    private final JSONArray tagsArray;
    private final TagsSQL tagsSQL = new TagsSQL();


    private TagsCache() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(100)
                .build();
        this.tagsArray = new JSONArray();
    }

    public static TagsCache getInstance() {
        return INSTANCE;
    }


    public void reloadNow() {
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

    public void saveAllTags() {
        cache.put("tags", new JSONArray(tagsArray.toString()));
    }


    public JSONArray getAllTags() {
        JSONArray cached = cache.getIfPresent("tags");
        if (cached != null) {
            System.out.println(Color.WHITE_BOLD+"[JeezyDevelopment]"+Color.GREEN_BOLD+Color.RESET+" 📤 Tags retrieved from cache!");
            return cached;
        } else {
            System.out.println("❌ No tags found in cache.");
            return null;
        }
    }
}
