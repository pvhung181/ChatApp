package vn.pvhung.appchat.activities.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.pvhung.appchat.adapters.ChatAdapter;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityChatBinding;
import vn.pvhung.appchat.helpers.DateHelper;
import vn.pvhung.appchat.helpers.ImageHelper;
import vn.pvhung.appchat.models.ChatMessage;
import vn.pvhung.appchat.models.User;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    private User userReceiver;
    ChatAdapter chatAdapter;
    List<ChatMessage> chatMessages;
    UserPreferenceManager userPreferenceManager;
    FirebaseFirestore database;

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessages.size();
            value.getDocumentChanges().forEach(doc -> {
                if (doc.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chat = new ChatMessage();
                    chat.setMessage(doc.getDocument().getString(StringConstants.KEY_MESSAGE));
                    chat.setSenderId(doc.getDocument().getString(StringConstants.KEY_SENDER_ID));
                    chat.setReceiverId(doc.getDocument().getString(StringConstants.KEY_RECEIVER_ID));
                    chat.setDatetime(DateHelper.getReadableDate(doc.getDocument().getDate(StringConstants.KEY_TIMESTAMP)));
                    chat.setDateObj(doc.getDocument().getDate(StringConstants.KEY_TIMESTAMP));

                    chatMessages.add(chat);
                }
            });

            chatMessages.sort(Comparator.comparing(ChatMessage::getDateObj));

            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeChanged(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }

            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }

        binding.progressBar.setVisibility(View.GONE);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loading(true);
        loadUserReceiver();
        init();
        setListener();
        listenMessage();
    }

    private void setListener() {
        binding.layoutSend.setOnClickListener(v -> sendMessage());
        binding.backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

    }

    private void listenMessage() {
        database.collection(StringConstants.KEY_COLLECTIONS_CHAT)
                .whereEqualTo(StringConstants.KEY_SENDER_ID, userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID))
                .whereEqualTo(StringConstants.KEY_RECEIVER_ID, userReceiver.getUserId())
                .addSnapshotListener(eventListener);

        database.collection(StringConstants.KEY_COLLECTIONS_CHAT)
                .whereEqualTo(StringConstants.KEY_RECEIVER_ID, userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID))
                .whereEqualTo(StringConstants.KEY_SENDER_ID, userReceiver.getUserId())
                .addSnapshotListener(eventListener);
    }

    private void loadUserReceiver() {
        userReceiver = (User) getIntent().getSerializableExtra(StringConstants.KEY_USER);
        if (userReceiver != null) binding.username.setText(userReceiver.getDisplayName());
    }

    private void init() {
        userPreferenceManager = new UserPreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID),
                ImageHelper.decodeImage(userReceiver.getImage())
        );

        binding.chatRecyclerView.setAdapter(chatAdapter);
//        database.collection(StringConstants.KEY_COLLECTIONS_CHAT)
//                .whereIn(
//                        StringConstants.KEY_SENDER_ID,
//                        Arrays.asList(userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID), userReceiver.getUserId())
//                )
//                .get()
//                .addOnSuccessListener(task -> {
//                    task.getDocuments().forEach(m -> {
//
//                    });
//
//
//                    binding.chatRecyclerView.setAdapter(chatAdapter);
//                    binding.chatRecyclerView.setVisibility(View.VISIBLE);
//                    loading(false);
//
//                });


    }

    private void loading(boolean isLoading) {
        if (isLoading) binding.progressBar.setVisibility(View.VISIBLE);
        else binding.progressBar.setVisibility(View.INVISIBLE);
    }

    private void sendMessage() {
        if (binding.messageInput.getText().toString().trim().isEmpty()) return;
        Map<String, Object> messages = new HashMap<>();
        messages.put(StringConstants.KEY_SENDER_ID, userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID));
        messages.put(StringConstants.KEY_RECEIVER_ID, userReceiver.getUserId());
        messages.put(StringConstants.KEY_MESSAGE, binding.messageInput.getText().toString());
        messages.put(StringConstants.KEY_TIMESTAMP, new Date());

        database.collection(StringConstants.KEY_COLLECTIONS_CHAT)
                .add(messages)
                .addOnSuccessListener(s -> {
                    makeToast("Success");
                })
                .addOnFailureListener(e -> {
                    makeToast("Fail");
                })
        ;

        binding.messageInput.setText("");
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}