package vn.pvhung.appchat.fragments.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import vn.pvhung.appchat.activities.login.LoginActivity;
import vn.pvhung.appchat.constants.SharedPreferenceName;
import vn.pvhung.appchat.databinding.FragmentSettingBinding;
import vn.pvhung.appchat.util.preferenceManager.PreferenceManager;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    UserPreferenceManager userPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userPreferences = new UserPreferenceManager(getActivity().getApplicationContext());
        binding.signoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            userPreferences.clearAllUserInformation();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });
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