package vn.pvhung.appchat.activities.home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.time.temporal.ValueRange;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import vn.pvhung.appchat.R;
import vn.pvhung.appchat.activities.login.LoginActivity;
import vn.pvhung.appchat.activities.users.UsersActivity;
import vn.pvhung.appchat.constants.SharedPreferenceName;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityHomeBinding;
import vn.pvhung.appchat.fragments.friends.FriendFragment;
import vn.pvhung.appchat.fragments.home.HomeFragment;
import vn.pvhung.appchat.fragments.setting.SettingFragment;
import vn.pvhung.appchat.util.preferenceManager.PreferenceManager;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;
@AndroidEntryPoint

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    FirebaseUser currentUser;
    @Inject
    FirebaseFirestore firestore;
    UserPreferenceManager userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        userPreferences = new UserPreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        setupNavigationBottomAppBar();
        setListeners();
        getToken();
    }

    private void setListeners() {
        binding.addBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, UsersActivity.class));
        });
    }

    private void setupNavigationBottomAppBar() {

        setColorForTextViewWithDrawableTop(binding.chat, R.color.md_theme_light_primary);

        binding.chat.setOnClickListener(v -> {
            if (!(getVisibleFragment() instanceof HomeFragment))
                setFragment(new HomeFragment());

            setColorForTextViewWithDrawableTop(binding.chat, R.color.md_theme_light_primary);
            setColorForTextViewWithDrawableTop(binding.people, R.color.black);
            setColorForTextViewWithDrawableTop(binding.setting, R.color.black);
        });

        binding.people.setOnClickListener(v -> {
            if (!(getVisibleFragment() instanceof FriendFragment))
                setFragment(new FriendFragment());

            setColorForTextViewWithDrawableTop(binding.people, R.color.md_theme_light_primary);
            setColorForTextViewWithDrawableTop(binding.chat, R.color.black);
            setColorForTextViewWithDrawableTop(binding.setting, R.color.black);
        });

        binding.setting.setOnClickListener(v -> {
            if (!(getVisibleFragment() instanceof SettingFragment))
                setFragment(new SettingFragment());

            setColorForTextViewWithDrawableTop(binding.setting, R.color.md_theme_light_primary);
            setColorForTextViewWithDrawableTop(binding.people, R.color.black);
            setColorForTextViewWithDrawableTop(binding.chat, R.color.black);
        });
    }

    private void setFragment(Fragment fr) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.section_fragment, fr).commit();
    }

    private void setColorForTextViewWithDrawableTop(TextView tv, @ColorRes int color) {
        tv.setTextColor(getColor(color));
        tv.getCompoundDrawables()[1].setColorFilter(getColor(color), PorterDuff.Mode.SRC_ATOP);
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateFcmToken);
    }

    private void updateFcmToken(String token) {
        DocumentReference df =  firestore.collection(StringConstants.KEY_COLLECTIONS_USER)
                .document(userPreferences.getString(StringConstants.KEY_DOCUMENT_ID));

        df.update(StringConstants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(v -> makeToast("Token updated successfully"))
                .addOnFailureListener(e -> makeToast("Fail to update token"));
    }

    public void signout() {
        DocumentReference df =  firestore.collection(StringConstants.KEY_COLLECTIONS_USER)
                .document(userPreferences.getString(StringConstants.KEY_DOCUMENT_ID));

        df.update(StringConstants.KEY_FCM_TOKEN, FieldValue.delete())
                .addOnSuccessListener(v -> {
                    makeToast("Token deleted successfully");
                    FirebaseAuth.getInstance().signOut();
                    userPreferences.clearAllUserInformation();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> makeToast("Fail to delete token"));
    }

}