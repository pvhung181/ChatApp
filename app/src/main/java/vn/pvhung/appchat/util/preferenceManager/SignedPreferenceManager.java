package vn.pvhung.appchat.util.preferenceManager;

import android.content.Context;

import dagger.hilt.android.qualifiers.ApplicationContext;
import vn.pvhung.appchat.constants.SharedPreferenceName;

public class SignedPreferenceManager extends PreferenceManager{

    public SignedPreferenceManager(@ApplicationContext Context context) {
        super(SharedPreferenceName.SIGNED_IN_ACCOUNT, context);
    }
}
