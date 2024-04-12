package vn.pvhung.appchat.util.preferenceManager;

import android.content.Context;

import dagger.hilt.android.qualifiers.ApplicationContext;
import vn.pvhung.appchat.constants.SharedPreferenceName;

public class AppPreferenceManager extends PreferenceManager{
    public AppPreferenceManager(@ApplicationContext Context context) {
        super(SharedPreferenceName.KEY_APP_PREFERENCES, context);
    }
}
