package vn.pvhung.appchat.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import vn.pvhung.appchat.MainActivity;
import vn.pvhung.appchat.R;
import vn.pvhung.appchat.activities.home.HomeActivity;
import vn.pvhung.appchat.activities.register.RegisterActivity;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient gClient;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        signInWithGoogle(account.getIdToken());

                    } catch (ApiException e) {
                        Log.e(StringConstants.GOOGLE_SIGNIN_TAG, "Failed to sign in with google");
                        throw new RuntimeException(e);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gClient = GoogleSignIn.getClient(this, gso);

        setListeners();
    }

    private void setListeners() {
        binding.signinButton.setOnClickListener(v -> {
            if(!binding.usernameInput.isActivated() || !binding.passwordInput.isActivated()) return;
            if(binding.usernameInput.getText() == null || binding.passwordInput.getText() == null) return;
            String email = binding.usernameInput.getText().toString();
            String pass = binding.passwordInput.getText().toString();
            binding.loadingPanel.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(task -> {
                        startActivity(new Intent(this, HomeActivity.class));
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Username/Password is wrong !!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnCompleteListener(l -> {
                                binding.loadingPanel.setVisibility(View.GONE);
                            }
                    );
        });


        binding.googleButton.setOnClickListener(v -> {
            binding.loadingPanel.setVisibility(View.VISIBLE);
            Intent it = gClient.getSignInIntent();
            launcher.launch(it);
        });

        binding.backToWelcomeBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        binding.signupLabel.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    public void signInWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(suc -> {
                    Log.e(StringConstants.GOOGLE_SIGNIN_TAG, suc.getUser().getEmail());
                    startActivity(new Intent(this, HomeActivity.class));
                })
                .addOnFailureListener(fail -> {
                    Log.e(StringConstants.GOOGLE_SIGNIN_TAG, "Failed to sign in with google");
                    Toast.makeText(this, "Fail to sign in with google", Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(task -> {
                    binding.loadingPanel.setVisibility(View.GONE);
                });

    }

    public void signInWithFacebook() {
        //TODO
    }
}