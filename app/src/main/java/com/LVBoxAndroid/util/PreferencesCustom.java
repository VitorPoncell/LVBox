package com.LVBoxAndroid.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesCustom {

    private Context context;
    private SharedPreferences preferences;
    private final String FILE_NAME = "lvbox.preferences";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;
    private final String INDENTIFIER_KEY = "logedUserIdentifier";
    private final String NAME_KEY = "logedUserName";
    private final String EMAIL_KEY = "logedUserEmail";

    public PreferencesCustom(Context context){
        this.context = context;
        preferences = this.context.getSharedPreferences(FILE_NAME,MODE);
        editor = preferences.edit();
    }

    public void saveData(String userIdentifier ,String userName, String userEmail){
        editor.putString(INDENTIFIER_KEY,userIdentifier);
        editor.putString(NAME_KEY,userName);
        editor.putString(EMAIL_KEY,userEmail);
        editor.commit();
    }

    public String getIdentifier(){
        return preferences.getString(INDENTIFIER_KEY,null);
    }

    public String getName(){
        return preferences.getString(NAME_KEY,null);
    }

    public String getEmail(){
        return preferences.getString(EMAIL_KEY,null);
    }
}
