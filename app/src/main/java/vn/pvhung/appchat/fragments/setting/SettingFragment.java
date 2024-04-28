package vn.pvhung.appchat.fragments.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.internal.ComponentEntryPoint;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import vn.pvhung.appchat.R;
import vn.pvhung.appchat.activities.home.HomeActivity;
import vn.pvhung.appchat.activities.login.LoginActivity;
import vn.pvhung.appchat.constants.SharedPreferenceName;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.FragmentSettingBinding;
import vn.pvhung.appchat.helpers.ImageHelper;
import vn.pvhung.appchat.util.preferenceManager.PreferenceManager;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

@AndroidEntryPoint
public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    UserPreferenceManager userPreferences;
    @Inject
    FirebaseAuth auth;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userPreferences = new UserPreferenceManager(requireActivity().getApplicationContext());
        //user = auth.getCurrentUser();
        setListeners();


//        if(user != null) {
//            Picasso.get()
//                    .load(user.getPhotoUrl())
//                    .transform(new CropCircleTransformation())
//                    .error(R.drawable.baseline_error_outline_24)
//                    .placeholder(R.drawable.baseline_replay_24)
//                    .into(binding.avatar);
//
//            binding.email.setText(user.getEmail());
//            binding.displayName.setText(user.getDisplayName());
//            binding.photoUrl.setText(user.getPhotoUrl().toString());
//
//        }
        if(userPreferences.getString(StringConstants.KEY_AVATAR) != null) {
            binding.email.setText(userPreferences.getString(StringConstants.KEY_EMAIL));
            binding.displayName.setText(userPreferences.getString(StringConstants.KEY_DISPLAY_NAME));
            //binding.photoUrl.setText(userPreferences.getString(StringConstants.KEY_AVATAR));
            binding.avatar.setImageBitmap(ImageHelper.decodeImage(userPreferences.getString(StringConstants.KEY_AVATAR)));
        }
    }

    public void setListeners() {
        binding.signoutBtn.setOnClickListener(v -> {
            ((HomeActivity)requireActivity()).signout();
        });
    }

    private void makeToast(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment tag", "On pause call in setting fragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Fragment tag", "onStart call in setting fragment");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment tag", "onStop call in setting fragment");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment tag", "onResume call in setting fragment");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment tag", "onDestroy call in setting fragment");

    }
}