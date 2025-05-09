package de.jeezycore.db.services;

import de.jeezycore.db.PlayersSQL;
import de.jeezycore.db.cache.PlayersCache;

import java.util.concurrent.CompletableFuture;

public class PlayersService {

    private final PlayersSQL playersSQL = new PlayersSQL();
    private final PlayersCache playersCache = PlayersCache.getInstance();

    public void load() {
        CompletableFuture.runAsync(playersSQL::getAllPlayerData);
    }

    public org.json.JSONArray getAllPlayerData() {
        return playersCache.getAllPlayerData();
    }

}
