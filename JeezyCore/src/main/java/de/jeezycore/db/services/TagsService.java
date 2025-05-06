package de.jeezycore.db.services;

import de.jeezycore.db.TagsSQL;
import de.jeezycore.db.cache.TagsCache;
import org.json.JSONArray;


public class TagsService {

    private final TagsCache tagsCache = TagsCache.getInstance();
    private final TagsSQL tagsSQL = new TagsSQL();

    public void load() {
        tagsSQL.getData();
        tagsSQL.getAllPlayerTags();
    }

    public JSONArray getAllTags() {
        return tagsCache.getAllTags();
    }

    public JSONArray getAllPlayerTags() {
        return tagsCache.getAllPlayerTags();
    }

}
