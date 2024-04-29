package vn.pvhung.appchat.activities.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import vn.pvhung.appchat.R;
import vn.pvhung.appchat.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}