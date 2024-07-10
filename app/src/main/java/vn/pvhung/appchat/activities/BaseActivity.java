package vn.pvhung.appchat.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class BaseActivity extends AppCompatActivity {
    FirebaseFirestore database;
    protected DocumentReference dr;
    protected UserPreferenceManager userPreferenceManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
        userPreferenceManager = new UserPreferenceManager(getApplicationContext());
        dr = database.collection(StringConstants.KEY_COLLECTIONS_USER)
                .document(userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID));
    }

    @Override
    protected void onResume() {
        super.onResume();
        dr.update(StringConstants.KEY_STATUS, 1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dr.update(StringConstants.KEY_STATUS, 0);
    }
}
