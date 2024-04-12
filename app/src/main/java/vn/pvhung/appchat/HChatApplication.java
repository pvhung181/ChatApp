package vn.pvhung.appchat;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

import com.cometchat.chat.core.AppSettings;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.User;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.UIKitSettings;

import dagger.hilt.android.HiltAndroidApp;
import vn.pvhung.appchat.constants.StringConstants;

@HiltAndroidApp
public class HChatApplication extends Application {

    AppSettings appSettings = new AppSettings.AppSettingsBuilder()
            .subscribePresenceForAllUsers()
            .setRegion(StringConstants.REGION)
            .autoEstablishSocketConnection(true)
            .build();

    @Override
    public void onCreate() {
        super.onCreate();



    }

    private void initCometChat() {
        CometChat.init(this, StringConstants.APP_ID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d(StringConstants.COMET_TAG, "Initialization completed successfully");
            }

            @Override
            public void onError(CometChatException e) {
                Log.e(StringConstants.COMET_TAG, "Initialization failed with exception: " + e.getMessage());
            }
        });
    }

    private void initCometChatUiKit() {
        UIKitSettings uiKitSettings = new UIKitSettings.UIKitSettingsBuilder()
                .setRegion(StringConstants.REGION)
                .setAppId(StringConstants.APP_ID)
                .setAuthKey(StringConstants.AUTH_KEY)
                .subscribePresenceForAllUsers().build();

        CometChatUIKit.init(this, uiKitSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successString) {
                Log.d(StringConstants.COMET_TAG, "Initialized Cometchat ui kit successfully");
            }

            @Override
            public void onError(CometChatException e) {
                Log.e(StringConstants.COMET_TAG, "Error when initialized Cometchat ui kit");
            }

        });
    }

    private void logoutCometChat() {
        CometChat.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                Log.d(StringConstants.COMET_TAG, "Logout completed successfully");
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(StringConstants.COMET_TAG, "Logout failed with exception: " + e.getMessage());
            }
        });
    }
}
