package vn.pvhung.appchat.activities.home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import vn.pvhung.appchat.R;
import vn.pvhung.appchat.constants.SharedPreferenceName;
import vn.pvhung.appchat.databinding.ActivityHomeBinding;
import vn.pvhung.appchat.fragments.friends.FriendFragment;
import vn.pvhung.appchat.fragments.home.HomeFragment;
import vn.pvhung.appchat.fragments.setting.SettingFragment;
import vn.pvhung.appchat.util.preferenceManager.PreferenceManager;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseUser currentUser;

    PreferenceManager userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        userPreferences = new UserPreferenceManager(getApplicationContext());

        setContentView(binding.getRoot());

        setupNavigationBottomAppBar();
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
}