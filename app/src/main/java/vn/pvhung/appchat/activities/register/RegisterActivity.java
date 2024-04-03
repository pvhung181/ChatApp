package vn.pvhung.appchat.activities.register;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import vn.pvhung.appchat.activities.home.HomeActivity;
import vn.pvhung.appchat.activities.login.LoginActivity;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;

    Pattern emailPattern = Pattern.compile(StringConstants.EMAIL_REGEX);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();


        binding.usernameInput.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!emailPattern.matcher(s.toString()).find()) binding.usernameInputLayout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.RED));
                        else binding.usernameInputLayout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.GREEN));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );

        binding.repasswordInput.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!isMatchPassword()) binding.repasswordInputLayout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.RED));
                        else binding.repasswordInputLayout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.GREEN));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                }
        );

        binding.loginLabel.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        binding.signupBtn.setOnClickListener(
                v -> registerNewUser()
        );

    }

    private void registerNewUser() {
        String email, password, repassword;
        email = binding.usernameInput.getText().toString();
        password = binding.passwordInput.getText().toString();
        repassword = binding.repasswordInput.getText().toString();

        if(isEmpty()) {
            Toast.makeText(this,"Please enter input!!",Toast.LENGTH_LONG).show();
            return;
        }

        if(!isMatchPassword()) {
            Toast.makeText(this,"Password don't match",Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                        "Registration successful!",
                                        Toast.LENGTH_LONG)
                                .show();

                        Intent intent
                                = new Intent(this,
                                HomeActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(
                                        getApplicationContext(),
                                        "Registration failed!!"
                                                + " Please try again later",
                                        Toast.LENGTH_LONG)
                                .show();
                    }
                });

    }

    private boolean isEmpty() {
        return  TextUtils.isEmpty(binding.usernameInput.getText().toString()) ||
                TextUtils.isEmpty(binding.passwordInput.getText().toString()) ||
                TextUtils.isEmpty(binding.repasswordInput.getText().toString());
    }

    private boolean isMatchPassword() {
        return binding.passwordInput.getText().toString().equals(binding.repasswordInput.getText().toString());
    }


}