package vn.pvhung.appchat.util;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.hilt.android.qualifiers.ApplicationContext;
import vn.pvhung.appchat.constants.SharedPreferenceName;

public class PreferenceManager {
    SharedPreferences sharedPreferences;

    public PreferenceManager(String preferenceName, @ApplicationContext Context context) {
        if(preferenceName.equals(SharedPreferenceName.SIGNED_IN_ACCOUNT)) {
            sharedPreferences = context.getSharedPreferences(SharedPreferenceName.SIGNED_IN_ACCOUNT, Context.MODE_PRIVATE);
        }
        else if(preferenceName.equals(SharedPreferenceName.KEY_PREFERENCE_NAME)) {
            sharedPreferences = context.getSharedPreferences(SharedPreferenceName.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
    }

    public void putString(String key, String val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public void putBoolean(String key, Boolean val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }


    public void clear() {
        sharedPreferences.edit().clear().apply();
    }


}
