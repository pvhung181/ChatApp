package vn.pvhung.appchat;

import android.app.Application;
import android.util.Log;


import dagger.hilt.android.HiltAndroidApp;
import vn.pvhung.appchat.constants.StringConstants;

@HiltAndroidApp
public class HChatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
