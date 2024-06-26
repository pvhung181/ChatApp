package vn.pvhung.appchat.util.preferenceManager;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.hilt.android.qualifiers.ApplicationContext;
import vn.pvhung.appchat.constants.SharedPreferenceName;
import vn.pvhung.appchat.constants.StringConstants;

public abstract class PreferenceManager {
    protected SharedPreferences sharedPreferences;

    public PreferenceManager(String preferenceName, @ApplicationContext Context context) {

        if(preferenceName.equals(SharedPreferenceName.SIGNED_IN_ACCOUNT)) {
            sharedPreferences = context.getSharedPreferences(SharedPreferenceName.SIGNED_IN_ACCOUNT, Context.MODE_PRIVATE);
        }
        else if(preferenceName.equals(SharedPreferenceName.KEY_APP_PREFERENCES)) {
            sharedPreferences = context.getSharedPreferences(SharedPreferenceName.KEY_APP_PREFERENCES, Context.MODE_PRIVATE);
        }
        else if (preferenceName.equals(SharedPreferenceName.USER_INFOR_PREFERENCES)) {
            sharedPreferences = context.getSharedPreferences(SharedPreferenceName.USER_INFOR_PREFERENCES, Context.MODE_PRIVATE);
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

    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


    public void clear() {
        sharedPreferences.edit().clear().apply();
    }


}
