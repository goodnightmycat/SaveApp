package com.example.saveapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.saveapp.ContextHolder;

public class SharePreferenceUtil {
    public static final String KEY_SP = "saveApp";
    public static final String KEY_ENTRANCE = "entrance_has_show";

    public static String readString(String key, String defaultValue) {
        return getSharedPreference().getString(key,defaultValue);
    }

    public static long readLong(String key, long defaultValue) {
        return getSharedPreference().getLong(key,defaultValue);
    }

    public static boolean readBoolean(String key, boolean defaultValue) {
        return getSharedPreference().getBoolean(key,defaultValue);
    }

    public static boolean remove(String key) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean write(String key,String value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(key,value);
        return editor.commit();
    }

    public static boolean write(String key,boolean b) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putBoolean(key,b);
        return editor.commit();
    }

    public static boolean write(String key,long value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putLong(key,value);
        return editor.commit();
    }

    private static SharedPreferences getSharedPreference() {
        return ContextHolder.getContext().getSharedPreferences(KEY_SP, Context.MODE_PRIVATE);
    }

    public static class Constants {
        public static final String CHECK_UPDATE = "check_update";
    }

}
