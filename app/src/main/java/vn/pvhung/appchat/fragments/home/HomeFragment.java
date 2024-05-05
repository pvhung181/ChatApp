package vn.pvhung.appchat.fragments.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import vn.pvhung.appchat.adapters.RecentConversationAdapter;
import vn.pvhung.appchat.databinding.FragmentHomeBinding;
import vn.pvhung.appchat.models.ChatMessage;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    Activity homeActivity;

    List<ChatMessage> conversations;
    FirebaseFirestore database;
    RecentConversationAdapter recentConversationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeActivity = requireActivity();
        init();
    }

    private void init() {
        database = FirebaseFirestore.getInstance();
        conversations = new ArrayList<>();
        recentConversationAdapter = new RecentConversationAdapter(conversations);
        binding.recentConversationRecycler.setAdapter(recentConversationAdapter);
    }

}