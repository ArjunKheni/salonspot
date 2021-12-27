package com.scet.saloonspot.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.scet.saloonspot.models.User;

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

    public static void setStringValue(Context context,String key, String value){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringValue(Context context, String key){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        return  pref.getString(key, "");
    }

    public static void storeUser(Context context,String key, User user){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor prefsEditor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public static User getUser(Context context, String key){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        Gson gson = new Gson();
        String json = pref.getString(key, "");
        User obj = gson.fromJson(json, User.class);
        return obj;
    }
}
