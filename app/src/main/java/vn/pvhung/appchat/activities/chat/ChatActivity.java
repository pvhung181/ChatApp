package vn.pvhung.appchat.activities.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import vn.pvhung.appchat.R;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityChatBinding;
import vn.pvhung.appchat.models.User;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loading(true);
        initUserData();

    }

    private void initUserData() {
        User user = (User) getIntent().getSerializableExtra(StringConstants.KEY_USER);
        if(user != null)
            binding.username.setText(user.getDisplayName());
    }

    private void loading(boolean isLoading) {
        if(isLoading)  binding.progressBar.setVisibility(View.VISIBLE);
        else binding.progressBar.setVisibility(View.INVISIBLE);
    }
}