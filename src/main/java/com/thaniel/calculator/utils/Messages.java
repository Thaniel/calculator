package com.thaniel.calculator.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
    private static ResourceBundle bundle = ResourceBundle.getBundle("properties/messages", Locale.ENGLISH);

    public static String get(String key) {
        return bundle.getString(key);
    }
}