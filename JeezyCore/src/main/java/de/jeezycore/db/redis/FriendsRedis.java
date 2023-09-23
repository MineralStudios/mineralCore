package de.jeezycore.db.redis;

import de.jeezycore.config.JeezyConfig;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class FriendsRedis {

    public static JedisPool pool;

    public static Jedis jedis;

    MemorySection config = (MemorySection) JeezyConfig.redis_defaults.get("REDIS");

    public boolean friendsAlreadyJoinedStatus;

    public void establishRedisPool() {
        pool = new JedisPool((String) config.get("ip"), (Integer) config.get("port"), (String) config.get("user"), (String) config.get("password"));
    }

    public void setFriendsMapOnStartup() {
        establishRedisPool();
        try (Jedis jedis = pool.getResource()) {
            Map<String, String> hash = new HashMap<>();
            hash.put("placeholder", "placeholder");
            jedis.hset("friends-already-joined", hash);
            jedis.hdel("friends-already-joined", "placeholder");
            hash.clear();
            pool.close();
        }
    }

    public void setFriendsOnJoin(Player p) {
        establishRedisPool();
        try (Jedis jedis = pool.getResource()) {
            Map<String, String> hash = new HashMap<>();
            hash.put(p.getUniqueId().toString(), p.getDisplayName());
            jedis.hset("friends-already-joined", hash);
            pool.close();
        }
    }

    public void getFriendsOnJoinData(Player p) {
        establishRedisPool();
        try (Jedis jedis = pool.getResource()) {
            if (jedis.hgetAll("friends-already-joined").containsKey(p.getUniqueId().toString())) {
                friendsAlreadyJoinedStatus = true;
            } else {
                friendsAlreadyJoinedStatus = false;
            }
            pool.close();
        }
    }

    public void deleteRedisFriendsHashOnQuit(Player p) {
        establishRedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.hdel("friends-already-joined", p.getUniqueId().toString());
            pool.close();
        }
    }
}