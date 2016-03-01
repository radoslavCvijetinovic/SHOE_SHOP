/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.fx.model;

import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Rajko
 */
public final class LanguageModel extends Observable {

    private ResourceBundle bundle;
    private Language lang;

    public LanguageModel() {
        setBundle(Language.SR);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public String translate(TranslationKeys key) {
        if (key == null) {
            throw new IllegalArgumentException("Translation key can not be empty!");
        }
        String result = bundle.getString(key.getKey());
        return StringUtils.isNotBlank(result) ? result : key.getKey();
    }

    public void setBundle(Language lang) {
        if (lang == null || lang.equals(this.bundle)) {
            return;
        }
        setLanguage(lang);
        bundle = ResourceBundle.getBundle("lang", new Locale(lang.getValue(), lang.toString()));
        setChanged();
        notifyObservers();
    }

    public Language getLanguage() {
        return lang;
    }

    private void setLanguage(Language lang) {
        this.lang = lang;
    }

    public enum Language {

        EN("en"),
        SR("sr");

        private final String value;

        private Language(String s) {
            value = s;
        }

        public String getValue() {
            return value;
        }

    }

    public enum TranslationKeys {

        USER_NOT_FOUND("message.user.not.found");

        private final String key;

        private TranslationKeys(String key) {
            this.key = key;
        }
         
        public String getKey() {
            return key;
        }
    }

}
