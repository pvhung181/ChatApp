package vn.pvhung.appchat.activities.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Objects;

import vn.pvhung.appchat.activities.BaseActivity;
import vn.pvhung.appchat.adapters.ChatAdapter;
import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ActivityChatBinding;
import vn.pvhung.appchat.helpers.DateHelper;
import vn.pvhung.appchat.helpers.ImageHelper;
import vn.pvhung.appchat.models.ChatMessage;
import vn.pvhung.appchat.models.User;
import vn.pvhung.appchat.util.preferenceManager.UserPreferenceManager;

public class ChatActivity extends BaseActivity {

    ActivityChatBinding binding;
    private User receiver;
    ChatAdapter chatAdapter;
    List<ChatMessage> chatMessages;
    UserPreferenceManager userPreferenceManager;
    FirebaseFirestore database;
    String conversationId = null;
    private boolean isReceiverActive = false;
    private final OnCompleteListener<QuerySnapshot> onCompleteConversationListener = task -> {
        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            conversationId = task.getResult().getDocuments().get(0).getId();
        }
    };

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

        if(conversationId == null) checkForConversation();
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
        listenStatusOfReceiver();
    }

    private void setListener() {
        binding.layoutSend.setOnClickListener(v -> sendMessage());
        binding.backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

    }

    private void listenMessage() {
        database.collection(StringConstants.KEY_COLLECTIONS_CHAT)
                .whereEqualTo(StringConstants.KEY_SENDER_ID, userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID))
                .whereEqualTo(StringConstants.KEY_RECEIVER_ID, receiver.getUserId())
                .addSnapshotListener(eventListener);

        database.collection(StringConstants.KEY_COLLECTIONS_CHAT)
                .whereEqualTo(StringConstants.KEY_RECEIVER_ID, userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID))
                .whereEqualTo(StringConstants.KEY_SENDER_ID, receiver.getUserId())
                .addSnapshotListener(eventListener);
    }

    private void loadUserReceiver() {
        receiver = (User) getIntent().getSerializableExtra(StringConstants.KEY_USER);
        if (receiver != null) binding.username.setText(receiver.getDisplayName());
    }

    private void init() {
        userPreferenceManager = new UserPreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID),
                ImageHelper.decodeImage(receiver.getImage())
        );

        binding.chatRecyclerView.setAdapter(chatAdapter);
    }

    private void loading(boolean isLoading) {
        if (isLoading) binding.progressBar.setVisibility(View.VISIBLE);
        else binding.progressBar.setVisibility(View.INVISIBLE);
    }

    private void sendMessage() {
        if (binding.messageInput.getText().toString().trim().isEmpty()) return;
        Map<String, Object> messages = new HashMap<>();
        messages.put(StringConstants.KEY_SENDER_ID, userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID));
        messages.put(StringConstants.KEY_RECEIVER_ID, receiver.getUserId());
        messages.put(StringConstants.KEY_MESSAGE, binding.messageInput.getText().toString());
        messages.put(StringConstants.KEY_TIMESTAMP, new Date());

        database.collection(StringConstants.KEY_COLLECTIONS_CHAT).add(messages);

        if(conversationId != null) updateConversation(binding.messageInput.getText().toString());
        else {
            HashMap<String, Object> cvs = new HashMap<>();
            cvs.put(StringConstants.KEY_SENDER_ID, userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID));
            cvs.put(StringConstants.KEY_SENDER_NAME, userPreferenceManager.getString(StringConstants.KEY_DISPLAY_NAME));
            cvs.put(StringConstants.KEY_SENDER_IMAGE, userPreferenceManager.getString(StringConstants.KEY_AVATAR));

            cvs.put(StringConstants.KEY_RECEIVER_ID, receiver.getUserId());
            cvs.put(StringConstants.KEY_RECEIVER_NAME, receiver.getDisplayName());
            cvs.put(StringConstants.KEY_RECEIVER_IMAGE, receiver.getImage());

            cvs.put(StringConstants.KEY_LAST_MESSAGE, binding.messageInput.getText().toString());

            cvs.put(StringConstants.KEY_TIMESTAMP, new Date());

            addConversation(cvs);
        }

        binding.messageInput.setText("");
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void addConversation(HashMap<String, Object> conversation) {
        database.collection(StringConstants.KEY_COLLECTIONS_CONVERSATION)
                .add(conversation)
                .addOnSuccessListener(s -> conversationId = s.getId());
    }

    private void updateConversation(String message) {
        database.collection(StringConstants.KEY_COLLECTIONS_CONVERSATION)
                .document(conversationId)
                .update(
                        StringConstants.KEY_LAST_MESSAGE, message,
                        StringConstants.KEY_TIMESTAMP, new Date()
                );
    }

    private void listenStatusOfReceiver() {
        database.collection(StringConstants.KEY_COLLECTIONS_USER)
                .document(receiver.getUserId())
                .addSnapshotListener(ChatActivity.this, (value, err) -> {
                    if(err != null) return;
                    if(value != null) {
                        if(value.getLong(StringConstants.KEY_STATUS) != null) {
                           int status = Objects.requireNonNull(value.getLong(StringConstants.KEY_STATUS)).intValue();
                           isReceiverActive = status == 1;
                        }
                        binding.status.setText(isReceiverActive ?  "online" : "offline");
                    }
                });
    }

    private void checkForConversation() {
        if(chatMessages.size() != 0) {
            checkForConversationRemotely(
                    userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID),
                    receiver.getUserId()
            );

            checkForConversationRemotely(
                    receiver.getUserId(),
                    userPreferenceManager.getString(StringConstants.KEY_DOCUMENT_ID)
            );
        }
    }

    private void checkForConversationRemotely(String senderId, String receiverId) {
        database.collection(StringConstants.KEY_COLLECTIONS_CONVERSATION)
                .whereEqualTo(StringConstants.KEY_SENDER_ID, senderId)
                .whereEqualTo(StringConstants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(onCompleteConversationListener);
    }
}