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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import vn.pvhung.appchat.MainActivity;
import vn.pvhung.appchat.R;
import vn.pvhung.appchat.activities.home.HomeActivity;
import vn.pvhung.appchat.activities.register.RegisterActivity;
import vn.pvhung.appchat.activities.user.UserInforCollectionActivity;
import vn.pvhung.appchat.constants.SharedPreferenceName;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityLoginBinding;
import vn.pvhung.appchat.util.Bcrypt;
import vn.pvhung.appchat.util.preferenceManager.PreferenceManager;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Inject
    FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient gClient;
    @Inject
    FirebaseFirestore firestore;
    UserPreferenceManager userPreferences;
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

        //firestore = FirebaseFirestore.getInstance();
        userPreferences = new UserPreferenceManager(getApplicationContext());

        //mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gClient = GoogleSignIn.getClient(this, gso);

        setListeners();
    }

    private void setListeners() {
        binding.signinButton.setOnClickListener(v -> {
            if (isValidInfor()) {
                binding.loadingPanel.setVisibility(View.VISIBLE);
                firestore.collection(StringConstants.KEY_COLLECTIONS_USER)
                        .whereEqualTo(StringConstants.KEY_USER_NAME, binding.usernameInput.getText().toString())
                        .get()
                        .addOnSuccessListener(s -> {
                            if (s.getDocuments().size() > 0) {
                                DocumentSnapshot doc = s.getDocuments().get(0);
                                if (!Bcrypt.checkPassword(binding.passwordInput.getText().toString(), doc.getString(StringConstants.KEY_PASSWORD))) {
                                    makeToast("Password is incorrect !!");
                                    binding.passwordInput.requestFocus();
                                } else {
                                    userPreferences.putBoolean(StringConstants.IS_SIGNED_IN, true);
                                    userPreferences.putString(StringConstants.KEY_USER_NAME, doc.getString(StringConstants.KEY_USER_NAME));
                                    userPreferences.putString(StringConstants.KEY_DOCUMENT_ID, doc.getId());
                                    //userPreferences.putBoolean(StringConstants.IS_FIRST_TIME, doc.getBoolean(StringConstants.IS_FIRST_TIME, true));

                                    if (!Boolean.TRUE.equals(doc.getBoolean(StringConstants.IS_FIRST_TIME))) {
                                        userPreferences.putBoolean(StringConstants.IS_FIRST_TIME, false);
                                        Intent it = new Intent(this, HomeActivity.class);
                                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(it);
                                    } else {
                                        userPreferences.putBoolean(StringConstants.IS_FIRST_TIME, true);
                                        Intent it = new Intent(this, UserInforCollectionActivity.class);
                                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(it);
                                    }


                                    finish();
                                }
                            } else {
                                makeToast("Username is incorrect !!");
                                binding.usernameInput.requestFocus();
                            }

                        })
                        .addOnFailureListener(e -> {
                            Log.e(StringConstants.LOGIN_ACTIVITY_TAG, e.getMessage());
                        })
                        .addOnCompleteListener(f -> {
                            binding.loadingPanel.setVisibility(View.GONE);
                        });
            }
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
        }
        return true;
    }


    public void signInWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(suc -> {
//                    firestore.collection(StringConstants.KEY_COLLECTIONS_USER)
//                            .whereEqualTo(StringConstants.KEY_USER_NAME, suc.getUser().getUid())
//                            .get()
//                            .addOnSuccessListener(s -> {
//                                if(s.getDocuments().size() == 0) {
//                                    Map<String, Object> data = new HashMap<>();
//                                    data.put(StringConstants.KEY_USER_NAME, suc.getUser().getUid());
//
//                                    firestore.collection(StringConstants.KEY_COLLECTIONS_USER)
//                                            .add(data);
//
//                                    Intent it = new Intent(this, UserInforCollectionActivity.class);
//                                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(it);
//                                }
//                                else {
//                                    DocumentSnapshot doc = s.getDocuments().get(0);
//                                    userPreferences.putString(StringConstants.KEY_DOCUMENT_ID, doc.getId());
//                                    if(Boolean.TRUE.equals(doc.getBoolean(StringConstants.IS_FIRST_TIME))) {
//                                        Intent it = new Intent(this, UserInforCollectionActivity.class);
//                                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(it);
//                                    }
//                                    else {
//                                        Intent it = new Intent(this, HomeActivity.class);
//                                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(it);
//                                    }
//                                }
//                            });

                    userPreferences.putAllNecessaryInfor(suc.getUser());

                    Intent it = new Intent(this, HomeActivity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                    binding.loadingPanel.setVisibility(View.GONE);
                    finish();
                })
                .addOnFailureListener(fail -> {
                    Log.e(StringConstants.GOOGLE_SIGNIN_TAG, "Failed to sign in with google");
                    binding.loadingPanel.setVisibility(View.GONE);
                    Toast.makeText(this, "Fail to sign in with google", Toast.LENGTH_SHORT).show();
                });
    }

    public void signInWithFacebook() {
        //TODO
    }
}