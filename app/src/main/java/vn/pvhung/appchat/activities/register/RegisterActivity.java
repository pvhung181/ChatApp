package vn.pvhung.appchat.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import vn.pvhung.appchat.activities.login.LoginActivity;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityRegisterBinding;
import vn.pvhung.appchat.util.Bcrypt;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    @Inject
    FirebaseAuth firebaseAuth;
    @Inject
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.loginLabel.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        binding.signupBtn.setOnClickListener(v -> registerNewUser());
    }

    private void registerNewUser() {
        String email, password;
        email = binding.usernameInput.getText().toString();
        password = binding.passwordInput.getText().toString();

        if (isValidInfor()) {
            binding.loadingPanel.setVisibility(View.VISIBLE);
            firestore.collection(StringConstants.KEY_COLLECTIONS_USER)
                    .whereEqualTo(StringConstants.KEY_USER_NAME, binding.usernameInput.getText().toString())
                    .get()
                    .addOnSuccessListener(suc -> {
                        if(suc.getDocuments().size() > 0) {
                            makeToast("Username already existed!");
                            binding.usernameInput.requestFocus();
                        }
                        else {
                            Map<String, Object> data = new HashMap<>();
                            data.put(StringConstants.KEY_USER_NAME, email);
                            data.put(StringConstants.IS_FIRST_TIME, true);
                            data.put(StringConstants.KEY_PASSWORD, Bcrypt.hashPassword(password));

                            firestore.collection(StringConstants.KEY_COLLECTIONS_USER).add(data).addOnSuccessListener(success -> {
                                Intent intent = new Intent(this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }).addOnFailureListener(e -> {
                                Log.e(StringConstants.REGISTER_ACTIVITY_TAG, e.getMessage());
                            });
                        }

                    })
                    .addOnFailureListener(e -> {
                        makeToast(e.getMessage());
                        binding.usernameInput.requestFocus();
                    })
                    .addOnCompleteListener(com -> {
                        binding.loadingPanel.setVisibility(View.GONE);
                    });
        }
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidInfor() {
        if (binding.usernameInput.getText().toString().trim().isEmpty()) {
            makeToast("User name must be filled");
            binding.usernameInput.requestFocus();
            return false;
        } else if (binding.passwordInput.getText().toString().trim().isEmpty()) {
            makeToast("Password must be filled");
            binding.passwordInput.requestFocus();
            return false;
        } else if (binding.repasswordInput.getText().toString().trim().isEmpty()) {
            makeToast("Password not match");
            binding.repasswordInput.requestFocus();
            return false;
        } else if (!binding.passwordInput.getText().toString().equals(binding.repasswordInput.getText().toString())) {
            makeToast("Password not match !");
            binding.repasswordInput.requestFocus();
            return false;
        }
        return true;
    }


}