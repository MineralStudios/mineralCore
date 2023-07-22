package de.jeezycore.utils;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;

public class CountryCodeChecker {

    public static String countryCode;

    public void check(String ip) {
        try {
            String url = "http://ip-api.com/json/" + ip;
            String Json = IOUtils.toString(new URL(url));
            JSONObject Object = (JSONObject) JSONValue.parseWithException(Json);
            countryCode = Object.get("countryCode").toString().toLowerCase();
            if (ArrayStorage.languageMap.get(countryCode.toLowerCase()) == null) {
                countryCode = "none";
            }
            System.out.println(countryCode);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
