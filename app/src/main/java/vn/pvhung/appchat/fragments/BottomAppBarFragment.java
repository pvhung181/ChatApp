package vn.pvhung.appchat.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.pvhung.appchat.R;
import vn.pvhung.appchat.activities.friends.FriendsActivity;
import vn.pvhung.appchat.activities.home.HomeActivity;
import vn.pvhung.appchat.activities.setting.SettingActivity;
import vn.pvhung.appchat.databinding.FragmentBottomAppBarBinding;

public class BottomAppBarFragment extends Fragment {

    FragmentBottomAppBarBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomAppBarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() instanceof HomeActivity) {
            binding.chat.setTextColor(getResources().getColor(R.color.md_theme_light_primary, null));
            setOnclickPeople();
            setOnclickSetting();
        }
        else if(getActivity() instanceof FriendsActivity) {
            binding.people.setTextColor(getResources().getColor(R.color.md_theme_light_primary, null));
            setOnClickChat();
            setOnclickSetting();
        }
        else {
            binding.setting.setTextColor(getResources().getColor(R.color.md_theme_light_primary, null));
            setOnClickChat();
            setOnclickPeople();
        }
    }

    private void setOnClickChat() {
        binding.chat.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), HomeActivity.class));
        });
    }

    private void setOnclickPeople() {
        binding.people.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), FriendsActivity.class));

        });
    }

    private void setOnclickSetting() {
        binding.setting.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        });
    }



}