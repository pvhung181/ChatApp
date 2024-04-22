package vn.pvhung.appchat.util.preferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.concurrent.Flow;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;
import vn.pvhung.appchat.constants.SharedPreferenceName;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.helpers.ImageHelper;

public class UserPreferenceManager extends PreferenceManager {

    public UserPreferenceManager(@ApplicationContext Context context) {
        super(SharedPreferenceName.USER_INFOR_PREFERENCES, context);
    }

    public void clearAllUserInformation() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(StringConstants.IS_SIGNED_IN);
        editor.remove(StringConstants.IS_FIRST_TIME);
        editor.remove(StringConstants.KEY_USER_NAME);
        editor.remove(StringConstants.KEY_EMAIL);
        editor.remove(StringConstants.KEY_AVATAR);
        editor.remove(StringConstants.KEY_CITY);
        editor.remove(StringConstants.KEY_ADDRESS);
        editor.remove(StringConstants.KEY_DISPLAY_NAME);
        editor.remove(StringConstants.KEY_DOCUMENT_ID);
        editor.apply();
    }

    public void putAllNecessaryInfor(FirebaseUser user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StringConstants.KEY_DISPLAY_NAME, user.getDisplayName());
        editor.putString(StringConstants.KEY_EMAIL, user.getEmail());
        editor.putString(StringConstants.KEY_AVATAR, user.getPhotoUrl().toString());
        Picasso.get().load(user.getPhotoUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                editor.putString(StringConstants.KEY_AVATAR, ImageHelper.encodeImage(bitmap));
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });
        editor.apply();
    }
}
