package vn.pvhung.appchat.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;

import vn.pvhung.appchat.R;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityUserDetailsBinding;
import vn.pvhung.appchat.helpers.DateHelper;
import vn.pvhung.appchat.helpers.ImageHelper;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class UserDetails extends AppCompatActivity {

    private ActivityUserDetailsBinding binding;
    private UserPreferenceManager userPreference;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        loadUserDetails();
        setListeners();
    }

    private void init() {
        userPreference = new UserPreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();
    }

    private void loadUserDetails() {
        binding.avatar.setImageBitmap(ImageHelper.decodeImage(userPreference.getString(StringConstants.KEY_AVATAR)));
        binding.nameEditText.setText(userPreference.getString(StringConstants.KEY_DISPLAY_NAME));
        binding.birthDay.setText(userPreference.getString(StringConstants.KEY_BIRTH));
        binding.emailEditText.setText(userPreference.getString(StringConstants.KEY_EMAIL));
        binding.cityEditText.setText(userPreference.getString(StringConstants.KEY_CITY));
        binding.addressEditText.setText(userPreference.getString(StringConstants.KEY_ADDRESS));
    }

    private void setListeners() {
        binding.birthDay.setOnClickListener(v -> {
            openDatePickerDialog();
        });

        binding.backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        binding.saveBtn.setOnClickListener(v -> {
            if(!isValidInfor()) return;
            isLoading(true);
            database.collection(StringConstants.KEY_COLLECTIONS_USER)
                    .document(userPreference.getString(StringConstants.KEY_DOCUMENT_ID))
                    .update(
                            StringConstants.KEY_DISPLAY_NAME, binding.nameEditText.getText().toString(),
                            StringConstants.KEY_BIRTH, binding.birthDay.getText().toString(),
                            StringConstants.KEY_EMAIL, binding.emailEditText.getText().toString(),
                            StringConstants.KEY_CITY, binding.cityEditText.getText().toString(),
                            StringConstants.KEY_ADDRESS, binding.addressEditText.getText().toString()
                    )
                    .addOnSuccessListener(s -> {
                        makeToast("Update successfully");
                        userPreference.putString(StringConstants.KEY_DISPLAY_NAME, binding.nameEditText.getText().toString());
                        userPreference.putString(StringConstants.KEY_BIRTH, binding.birthDay.getText().toString());
                        userPreference.putString(StringConstants.KEY_EMAIL, binding.emailEditText.getText().toString());
                        userPreference.putString(StringConstants.KEY_CITY, binding.cityEditText.getText().toString());
                        userPreference.putString(StringConstants.KEY_ADDRESS, binding.addressEditText.getText().toString());
                    })
                    .addOnCompleteListener(task -> {
                        isLoading(false);
                    });
        });
    }
    private void openDatePickerDialog() {
        LocalDate date = LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            binding.birthDay.setText(DateHelper.formatDate(dayOfMonth + "/" + month + "/" + year, "/", "/", DateHelper.DD_MM_YYYY));
        },date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        datePickerDialog.show();
    }

    private boolean isValidInfor() {
        if (binding.nameEditText.getText().toString().trim().isEmpty()) {
            makeToast("User name must be filled");
            binding.nameEditText.requestFocus();
            return false;
        } else if (binding.emailEditText.getText().toString().trim().isEmpty()) {
            makeToast("Email must be filled");
            binding.emailEditText.requestFocus();
            return false;
        }
        else if (binding.cityEditText.getText().toString().trim().isEmpty()) {
            makeToast("City must be filled");
            binding.cityEditText.requestFocus();
            return false;
        }
        else if (binding.addressEditText.getText().toString().trim().isEmpty()) {
            makeToast("Address must be filled");
            binding.addressEditText.requestFocus();
            return false;
        }
        else if (binding.birthDay.getText().toString().trim().isEmpty()) {
            makeToast("birthday must be choose");
            return false;
        }
        return true;
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void isLoading(boolean loading) {
        if(loading) {
            binding.saveBtn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.saveBtn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}