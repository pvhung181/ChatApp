package vn.pvhung.appchat.activities.users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import vn.pvhung.appchat.adapters.UsersAdapter;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityUsersBinding;
import vn.pvhung.appchat.models.User;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class UsersActivity extends AppCompatActivity {

    ActivityUsersBinding binding;
    UserPreferenceManager userPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        userPreferenceManager = new UserPreferenceManager(getApplicationContext());
        binding.notFound.setVisibility(View.INVISIBLE);
        setContentView(binding.getRoot());
        setListeners();
        getAllUsers();
    }

    private void loading(boolean isLoading) {
        if(isLoading)  binding.loadingProgressBar.setVisibility(View.VISIBLE);
        else binding.loadingProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showNotFound() {
        binding.notFound.setVisibility(View.VISIBLE);
    }

    private void setListeners() {
        binding.backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void getAllUsers() {
        loading(true);
        List<User> result = new ArrayList<>();
        FirebaseFirestore.getInstance().collection(StringConstants.KEY_COLLECTIONS_USER)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID);
                   if(task.isSuccessful() && task.getResult() != null) {
                       task.getResult().getDocuments().forEach(val -> {
                           if(!val.getId().equals(currentUserId)) {
                               User user = new User();
                               user.setDisplayName(val.getString(StringConstants.KEY_DISPLAY_NAME));
                               user.setUsername(val.getString(StringConstants.KEY_USER_NAME));
                               user.setImage(val.getString(StringConstants.KEY_AVATAR));
                               user.setToken(val.getString(StringConstants.KEY_FCM_TOKEN));

                               result.add(user);
                           }
                       });
                   }
                   loading(false);
                   if(!result.isEmpty()) {
                       UsersAdapter usersAdapter = new UsersAdapter(result);
                       binding.listUser.setAdapter(usersAdapter);
                   }
                   else {
                       showNotFound();
                   }


                });
    }

}