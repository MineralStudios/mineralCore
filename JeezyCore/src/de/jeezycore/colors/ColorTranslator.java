package de.jeezycore.colors;

import java.util.HashMap;

public class ColorTranslator {

    public static HashMap<Integer, String> colorTranslator = new HashMap<Integer, String>();


    static void addColors() {
        colorTranslator.put(0, "§f");
        colorTranslator.put(1, "§6");
        colorTranslator.put(2, "§c");
        colorTranslator.put(3, "§9");
        colorTranslator.put(4, "§e");
        colorTranslator.put(5, "§a");
        colorTranslator.put(6, "§d");
        colorTranslator.put(7, "§8");
        colorTranslator.put(8, "§7");
        colorTranslator.put(9, "§3");
        colorTranslator.put(10, "§5");
        colorTranslator.put(11, "§1");
        colorTranslator.put(12, "§6");
        colorTranslator.put(13, "§2");
        colorTranslator.put(14, "§4");
        colorTranslator.put(15, "§0");

    }
    static {
        addColors();
    }



}
