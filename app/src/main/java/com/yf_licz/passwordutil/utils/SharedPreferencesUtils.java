package com.yf_licz.passwordutil.utils;


import android.content.Context;
import android.content.SharedPreferences;


/**
 * @author yfzx-sh-licz
 * @date 2017/11/24
 */
public class SharedPreferencesUtils {

    private static SharedPreferences sp;

    private static Context mApplicationContext;

    private SharedPreferencesUtils() {
    }

    /**
     * 使用application context 在application初始化时，进行初始化
     */
    public static void init(Context applicationContext) {
        mApplicationContext = applicationContext.getApplicationContext();
        if (null == mApplicationContext) {
            mApplicationContext = applicationContext;
        }
    }

    private static void getInstance() {
        try {
            if (sp == null) {
                sp = mApplicationContext.getSharedPreferences(SecurityUtils.MD5_Encode("PasswordUtil"), Context.MODE_PRIVATE);
            }
        } catch (NullPointerException e) {
            throw new IllegalStateException("SharedPreferences 为空，请去globalInitAction中配置SharedPreferencesLoader.init()");
        }
    }

    public static void putString(String key, String value) {
        getInstance();
        sp.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        getInstance();
        return sp.getString(key, defaultValue);
    }

    public static String getString(String key) {
        getInstance();
        return sp.getString(key, "");
    }

    public static void putInt(String key, int value) {
        getInstance();
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        getInstance();
        return sp.getInt(key, 0);
    }

    public static void putBoolean(String key, boolean value) {
        getInstance();
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        getInstance();
        return sp.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        getInstance();
        return sp.getBoolean(key, defaultValue);
    }

}

