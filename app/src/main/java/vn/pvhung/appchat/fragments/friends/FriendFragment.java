package vn.pvhung.appchat.fragments.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vn.pvhung.appchat.databinding.FragmentFriendBinding;

public class FriendFragment extends Fragment {
    FragmentFriendBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment tag", "On pause call in friend fragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Fragment tag", "onStart call in friend fragment");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment tag", "onStop call in friend fragment");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment tag", "onResume call in friend fragment");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment tag", "onDestroy call in friend fragment");

    }
}