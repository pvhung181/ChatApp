package vn.pvhung.appchat.activities.users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import vn.pvhung.appchat.R;
import vn.pvhung.appchat.databinding.ActivityUsersBinding;

public class UsersActivity extends AppCompatActivity {

    ActivityUsersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }


}