package de.jeezycore.velocity.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;


import java.net.URL;
import java.util.Optional;
import java.util.logging.Logger;

public class UUIDCheckerVelocity {
    private final ProxyServer server;
    private final Logger logger;

    public UUIDCheckerVelocity(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

        public static String uuid;
        public static String uuidName;

        public void check(String userName) {

            Optional<Player> ps = server.getPlayer(userName);

            try {
                if (ps.isPresent()) {
                    uuid = ps.get().getUniqueId().toString().replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
                    uuidName = ps.get().getUsername();
                    System.out.println(uuid);
                    return;
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            try {
                String url = "https://api.mojang.com/users/profiles/minecraft/" + userName;
                String UUIDJson = IOUtils.toString(new URL(url));
                JSONObject json = new JSONObject(UUIDJson);
                uuid  = json.getString("id").toString().replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
                uuidName  = json.getString("name");
                System.out.println(uuid);
                System.out.println(uuidName);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }