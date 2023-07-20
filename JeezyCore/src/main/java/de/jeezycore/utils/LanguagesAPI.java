package de.jeezycore.utils;

import net.suuft.libretranslate.Language;
import net.suuft.libretranslate.Translator;

public class LanguagesAPI {


    public void setTranslateUrl() {
        Translator.setUrlApi("http://localhost:5000/translate");
    }

    public void setLanguages() {
        if (ArrayStorage.languageMap.isEmpty()) {
            for(Language env : Language.values()) {
                ArrayStorage.languageMap.put(env.getCode(), Language.valueOf(env.name()));
            }
        }
    }

    public String translate(String language, String messageToTranslate) {
        return Translator.translate(ArrayStorage.languageMap.get(language), messageToTranslate);
    }
}