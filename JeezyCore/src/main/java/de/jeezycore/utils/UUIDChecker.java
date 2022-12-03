package de.jeezycore.utils;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;

public class UUIDChecker {
        public static String uuid;
        public static String uuidName;

        public void check(String userName) {

                Player ps = Bukkit.getPlayer(userName);

                if (ps != null) {
                        uuid = ps.getUniqueId().toString().replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
                        uuidName = ps.getPlayer().getDisplayName();
                        System.out.println(uuid);
                        return;
                }
                        try {
                                String url = "https://api.mojang.com/users/profiles/minecraft/" + userName;
                                String UUIDJson = IOUtils.toString(new URL(url));
                                JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
                                uuid = UUIDObject.get("id").toString().replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
                                uuidName = UUIDObject.get("name").toString();
                                System.out.println(uuid);
                                System.out.println(uuidName);
                        } catch (Exception e) {
                                System.out.println(e);
                        }
                }
        }