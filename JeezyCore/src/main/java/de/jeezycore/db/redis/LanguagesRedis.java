package de.jeezycore.db.redis;

import de.jeezycore.config.JeezyConfig;
import de.jeezycore.utils.ArrayStorage;
import de.jeezycore.utils.CountryCodeChecker;
import net.suuft.libretranslate.Language;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class LanguagesRedis {

    public static JedisPool pool;

    public static Jedis jedis;

    MemorySection config = (MemorySection) JeezyConfig.redis_defaults.get("REDIS");

    CountryCodeChecker countryCodeChecker = new CountryCodeChecker();

    public void establishRedisPool() {
        if (!(Boolean) config.get("enabled")) {
            pool = new JedisPool((String) config.get("ip"), (Integer) config.get("port"), (String) config.get("user"), (String) config.get("password"));
        }
    }

    public void setLanguage(org.bukkit.event.inventory.InventoryClickEvent e, String language) {
        establishRedisPool();
        try (Jedis jedis = pool.getResource()) {
            String res1 = jedis.set(String.valueOf(e.getWhoClicked().getUniqueId()), language.substring(4));
            pool.close();
        }
    }

    public String getLanguage(Player p) {
        String lang = null;
        establishRedisPool();
        try (Jedis jedis = pool.getResource()) {
            for(String env : ArrayStorage.languageMap.keySet()) {

                if (jedis.get(String.valueOf(p.getPlayer().getUniqueId())) == null) {
                    lang = "none";
                    return lang;
                }

                if (ArrayStorage.languageMap.get(env).name().equalsIgnoreCase(jedis.get(String.valueOf(p.getPlayer().getUniqueId())).toUpperCase())) {
                    lang = ArrayStorage.languageMap.get(env).getCode();
                }
            }
            if (lang == null) {
                lang = "none";
            }
            pool.close();
        }
        return lang;
    }

    public void setLanguageAtJoin(Player p) {
        establishRedisPool();
        try (Jedis jedis = pool.getResource()) {
            countryCodeChecker.check(p.getPlayer().getAddress().getHostName());
            Language la = Language.valueOf(ArrayStorage.languageMap.get(CountryCodeChecker.countryCode).name());
            jedis.set(String.valueOf(p.getPlayer().getUniqueId()), String.valueOf(la));
            pool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}