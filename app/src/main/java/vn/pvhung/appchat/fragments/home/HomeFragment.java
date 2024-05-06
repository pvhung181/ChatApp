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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vn.pvhung.appchat.adapters.RecentConversationAdapter;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.FragmentHomeBinding;
import vn.pvhung.appchat.models.ChatMessage;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    Activity homeActivity;

    List<ChatMessage> conversations;
    FirebaseFirestore database;
    RecentConversationAdapter recentConversationAdapter;
    UserPreferenceManager userPreferenceManager;

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
        listenConversation();
    }


    private void init() {
        database = FirebaseFirestore.getInstance();
        conversations = new ArrayList<>();
        recentConversationAdapter = new RecentConversationAdapter(conversations);
        binding.recentConversationRecycler.setAdapter(recentConversationAdapter);
        userPreferenceManager = new UserPreferenceManager(homeActivity.getApplicationContext());
    }

    private void listenConversation() {
        database.collection(StringConstants.KEY_COLLECTIONS_CONVERSATION)
                .whereEqualTo(StringConstants.KEY_RECEIVER_ID, userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID))
                .addSnapshotListener(eventListener);

        database.collection(StringConstants.KEY_COLLECTIONS_CONVERSATION)
                .whereEqualTo(StringConstants.KEY_SENDER_ID, userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID))
                .addSnapshotListener(eventListener);
    }

    private EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null) return;
        if(value != null) {
            for(DocumentChange dc : value.getDocumentChanges()) {
                String senderId = dc.getDocument().getString(StringConstants.KEY_SENDER_ID);
                String receiverId = dc.getDocument().getString(StringConstants.KEY_RECEIVER_ID);
                if(dc.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage m = new ChatMessage();

                    m.setSenderId(senderId);
                    m.setReceiverId(receiverId);
                    m.setMessage(dc.getDocument().getString(StringConstants.KEY_LAST_MESSAGE));
                    m.setDateObj(dc.getDocument().getDate(StringConstants.KEY_TIMESTAMP));

                    if(senderId.equals(userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID))) {
                        m.setConversationId(dc.getDocument().getString(StringConstants.KEY_RECEIVER_ID));
                        m.setConversationName(dc.getDocument().getString(StringConstants.KEY_RECEIVER_NAME));
                        m.setConversationImage(dc.getDocument().getString(StringConstants.KEY_RECEIVER_IMAGE));
                    }
                    else {
                        m.setConversationId(dc.getDocument().getString(StringConstants.KEY_SENDER_ID));
                        m.setConversationName(dc.getDocument().getString(StringConstants.KEY_SENDER_NAME));
                        m.setConversationImage(dc.getDocument().getString(StringConstants.KEY_SENDER_IMAGE));
                    }

                    conversations.add(m);

                }
                else if(dc.getType() == DocumentChange.Type.MODIFIED) {
                    for(int i = 0; i < conversations.size(); i++) {
                        ChatMessage curr = conversations.get(i);
                        if(curr.getSenderId().equals(senderId) && curr.getReceiverId().equals(receiverId)) {
                            curr.setMessage(dc.getDocument().getString(StringConstants.KEY_LAST_MESSAGE));
                            curr.setDateObj(dc.getDocument().getDate(StringConstants.KEY_TIMESTAMP));
                            break;
                        }
                    }
                }
            }
        }

        //conversations.sort(Comparator.comparing(ChatMessage::getDateObj));

        binding.recentConversationRecycler.setVisibility(View.VISIBLE);
        binding.recentConversationRecycler.smoothScrollToPosition(0);
        binding.progressBar.setVisibility(View.GONE);
        recentConversationAdapter.notifyDataSetChanged();
    };


}