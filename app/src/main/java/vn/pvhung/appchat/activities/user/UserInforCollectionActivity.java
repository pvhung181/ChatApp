package vn.pvhung.appchat.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import vn.pvhung.appchat.constants.SharedPreferenceName;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityUserInforCollectionBinding;
import vn.pvhung.appchat.util.preferenceManager.PreferenceManager;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class UserInforCollectionActivity extends AppCompatActivity {

    ActivityUserInforCollectionBinding binding;
    PreferenceManager userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInforCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userPreferences = new UserPreferenceManager(getApplicationContext());

        setListeners();
    }

    private void setListeners() {
        binding.confirmButton.setOnClickListener(v -> {
            updateExistingDocument();
//            if(isValidInfor()) {
//
//            }

//            Intent it = new Intent(this, HomeActivity.class);
//            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(it);
//            finish();
        });

        binding.avatar.setOnClickListener(v -> {

        });
    }

    private boolean isValidInfor() {
        return false;
    }

    private void updateExistingDocument() {
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection(StringConstants.KEY_COLLECTIONS_USER)
                .document(userPreferences.getString(StringConstants.KEY_USER_NAME));

        Toast.makeText(this, docRef.getId(), Toast.LENGTH_SHORT).show();
    }
}