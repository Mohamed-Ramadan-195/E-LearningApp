package com.example.e_learningapp.data;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Context;
import com.example.e_learningapp.Const;

public class SharedPreference {
    private static SharedPreferences sharedPreferences = null;
    private static String AUTHENTICATION_NAME = "Authentication File";
    private static String SLIDE_NAME = "Slide File";
    private static SharedPreferences.Editor editor;

    public static void init(Context context, String apply){
        if (sharedPreferences == null) {
            if (apply.equals(Const.KEY_AUTHENTICATION)) {
                sharedPreferences = context.getSharedPreferences(AUTHENTICATION_NAME, Activity.MODE_PRIVATE);
            } else if (apply.equals(Const.KEY_SLIDE)) {
                sharedPreferences = context.getSharedPreferences(SLIDE_NAME, Activity.MODE_PRIVATE);
            }
        }
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void setData(String key, String value) {
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getData(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
    
    public static Boolean onBoardingRead() {
        return sharedPreferences.getBoolean(SLIDE_NAME,false);
    }

    public static void firstOpen() {
        editor = sharedPreferences.edit();
        editor.putBoolean(SLIDE_NAME,true);
        editor.commit();
    }
}