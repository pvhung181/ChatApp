package vn.pvhung.appchat.util.preferenceManager;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.hilt.android.qualifiers.ApplicationContext;
import vn.pvhung.appchat.constants.SharedPreferenceName;
import vn.pvhung.appchat.constants.StringConstants;

public class UserPreferenceManager extends PreferenceManager {

    public UserPreferenceManager(@ApplicationContext Context context) {
        super(SharedPreferenceName.USER_INFOR_PREFERENCES, context);
    }

    public void clearAllUserInformation() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(StringConstants.IS_SIGNED_IN);
        editor.remove(StringConstants.KEY_USER_NAME);
        editor.remove(StringConstants.KEY_AVATAR);
        editor.remove(StringConstants.KEY_DISPLAY_NAME);

        editor.apply();
    }
}
