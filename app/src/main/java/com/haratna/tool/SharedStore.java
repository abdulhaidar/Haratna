package com.haratna.tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.haratna.App;


/**
 * Created by Abdul on 2017/03/01.
 */

public class SharedStore {

    private static SharedStore   sharedPreference;
    public static final String PREFS_NAME = "Saqa_Prefs";
    public static final String PREFS_KEY = "Saqa_Prefs_String";

    public static SharedStore getInstance()
    {
        if (sharedPreference == null)
        {
            sharedPreference = new SharedStore();
        }
        return sharedPreference;
    }

    public SharedStore() {
        super();
    }

    public void save(String text , String Key) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = App.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(Key, text); //3

        editor.commit(); //4
    }

    public String getValue(String Key) {
        SharedPreferences settings;
        String text = "";
        //  settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = App.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(Key, "");
        return text;
    }

    public void clearSharedPreference() {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = App.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(String value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = App.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(value);
        editor.commit();
    }


}
