package vn.pvhung.appchat.activities.home;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import vn.pvhung.appchat.R;
import vn.pvhung.appchat.databinding.ActivityHomeBinding;
import vn.pvhung.appchat.fragments.friends.FriendFragment;
import vn.pvhung.appchat.fragments.home.HomeFragment;
import vn.pvhung.appchat.fragments.setting.SettingFragment;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        setupNavigationBottomAppBar();


    }

    private void setupNavigationBottomAppBar() {

        binding.chat.setOnClickListener(v -> setFragment(new HomeFragment()));

        binding.people.setOnClickListener(v -> setFragment(new FriendFragment()));

        binding.setting.setOnClickListener(v -> setFragment(new SettingFragment()));
    }

    private void setFragment(Fragment fr) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.section_fragment, fr).commit();
    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }
}