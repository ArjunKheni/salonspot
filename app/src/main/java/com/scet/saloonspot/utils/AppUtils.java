package com.scet.saloonspot.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppUtils {

    public static void setValue(Context context,String key, boolean value){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getValue(Context context, String key){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        return  pref.getBoolean(key, false);
    }
}
