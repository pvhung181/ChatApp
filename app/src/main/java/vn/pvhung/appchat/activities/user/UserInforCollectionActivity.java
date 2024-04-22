package vn.pvhung.appchat.activities.user;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.type.DateTime;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.ValueRange;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.pvhung.appchat.R;
import vn.pvhung.appchat.activities.home.HomeActivity;
import vn.pvhung.appchat.constants.SharedPreferenceName;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityUserInforCollectionBinding;
import vn.pvhung.appchat.helpers.DateHelper;
import vn.pvhung.appchat.helpers.ImageHelper;
import vn.pvhung.appchat.util.preferenceManager.PreferenceManager;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class UserInforCollectionActivity extends AppCompatActivity {

    ActivityUserInforCollectionBinding binding;
    PreferenceManager userPreferences;
    private String encodedImage = null;
    private ActivityResultLauncher<Intent> pickPhoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.avatar.setImageBitmap(bitmap);
                            encodedImage = ImageHelper.encodeImage(bitmap);
                            binding.addImageLabel.setVisibility(View.GONE);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

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
            if(!isValidInfor()) return;
            updateExistingDocument();


            Intent it = new Intent(this, HomeActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();
        });

        binding.avatar.setOnClickListener(v -> {
            Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickPhoto.launch(it);
        });

        binding.birthDay.setOnClickListener(v -> {
            openDatePickerDialog();
        });
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    private void updateExistingDocument() {
        CollectionReference cf = FirebaseFirestore.getInstance()
                .collection(StringConstants.KEY_COLLECTIONS_USER);

                cf.whereEqualTo(StringConstants.KEY_USER_NAME, userPreferences.getString(StringConstants.KEY_USER_NAME))
                .get().addOnSuccessListener(task -> {
                    DocumentSnapshot curr = task.getDocuments().get(0);
                    Map<String, Object> vals = new HashMap<>();
                    vals.put(StringConstants.KEY_DISPLAY_NAME, binding.nameEditText.getText().toString());
                    vals.put(StringConstants.KEY_EMAIL, binding.emailEditText.getText().toString());
                    vals.put(StringConstants.KEY_BIRTH, binding.birthDay.getText().toString());
                    vals.put(StringConstants.KEY_CITY, binding.cityEditText.getText().toString());
                    vals.put(StringConstants.KEY_ADDRESS, binding.addressEditText.getText().toString());
                    if(encodedImage != null) vals.put(StringConstants.KEY_AVATAR, encodedImage);
                    else {
                        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar);
                        vals.put(StringConstants.KEY_AVATAR, ImageHelper.encodeImage(icon));
                    }

                    cf.document(userPreferences.getString(StringConstants.KEY_DOCUMENT_ID)).set(vals, SetOptions.merge());
                });

        //Toast.makeText(this, docRef.getId(), Toast.LENGTH_SHORT).show();
    }

    private void openDatePickerDialog() {
        LocalDate date = LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            binding.birthDay.setText(DateHelper.formatDate(dayOfMonth + "/" + month + "/" + year, "/", "/", DateHelper.DD_MM_YYYY));
        },date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        datePickerDialog.show();
    }

}