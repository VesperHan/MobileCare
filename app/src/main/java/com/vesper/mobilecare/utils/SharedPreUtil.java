package com.vesper.mobilecare.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by vesperhan on 2017/3/25.
 */

public class SharedPreUtil {

    //  静态方法不属于任何对象
    private static SharedPreferences preferences;

    public static void putBoolean(Context ctx, String key, @NonNull Boolean value) {

        if (preferences == null) {
            preferences = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        preferences.edit().putBoolean(key, value).apply();
    }

    @NonNull
    public static Boolean getBoolean(Context ctx, String key, Boolean defValue) {

        if (preferences == null) {
            preferences = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return preferences.getBoolean(key, defValue);
    }

    public static void putString(Context context, String key, @NonNull String value) {

        if (preferences == null) {
            preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        preferences.edit().putString(key, value).apply();
    }

    @NonNull
    public static String getString(Context context, String key, String defValue) {

        if (preferences == null) {
            preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return preferences.getString(key, defValue);
    }

    public static void remove(Context context,String key){
        if (preferences == null){
            preferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        preferences.edit().remove(key).apply();
    }
}
