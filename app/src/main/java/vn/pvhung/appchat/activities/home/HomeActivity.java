package vn.pvhung.appchat.activities.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.pvhung.appchat.MainActivity;
import vn.pvhung.appchat.R;
import vn.pvhung.appchat.databinding.ActivityHomeBinding;
import vn.pvhung.appchat.databinding.ActivitySettingBinding;

public class HomeActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.signoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}