package vn.pvhung.appchat;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import vn.pvhung.appchat.activities.home.HomeActivity;
import vn.pvhung.appchat.activities.login.LoginActivity;
import vn.pvhung.appchat.activities.register.RegisterActivity;
import vn.pvhung.appchat.activities.user.UserInforCollectionActivity;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityMainBinding;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;

    UserPreferenceManager userPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreferenceManager = new UserPreferenceManager(getApplicationContext());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(
                v -> startActivity(new Intent(this, LoginActivity.class))

        );

        binding.signupButton.setOnClickListener(
                v -> startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null || userPreferenceManager.getBoolean(StringConstants.IS_SIGNED_IN)) {
            if(userPreferenceManager.getBoolean(StringConstants.IS_FIRST_TIME)) {
                Intent intent = new Intent(this, UserInforCollectionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MainActivity.this.finish();
            }
            else {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MainActivity.this.finish();
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }
}