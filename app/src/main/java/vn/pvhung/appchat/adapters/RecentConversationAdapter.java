package vn.pvhung.appchat.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import vn.pvhung.appchat.constants.StringConstants;
import vn.pvhung.appchat.databinding.ItemConversationContainerBinding;
import vn.pvhung.appchat.helpers.ImageHelper;
import vn.pvhung.appchat.listeners.UserListener;
import vn.pvhung.appchat.models.ChatMessage;
import vn.pvhung.appchat.models.User;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.RecentConversationViewHolder> {

    private final List<ChatMessage> chatMessages;
    private UserListener listener;

    public RecentConversationAdapter(List<ChatMessage> chatMessages, UserListener listener) {
        this.chatMessages = chatMessages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecentConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentConversationViewHolder(ItemConversationContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentConversationViewHolder holder, int position) {
        holder.setData(chatMessages.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class RecentConversationViewHolder extends RecyclerView.ViewHolder {
        private ItemConversationContainerBinding binding;

        public RecentConversationViewHolder(ItemConversationContainerBinding itemConversationContainerBinding) {
            super(itemConversationContainerBinding.getRoot());
            binding = itemConversationContainerBinding;
        }

        public void setData(ChatMessage message, UserListener listener) {
            binding.avatar.setImageBitmap(ImageHelper.decodeImage(message.getConversationImage()));
            binding.displayName.setText(message.getConversationName());
            binding.lastMessage.setText(message.getMessage());

            binding.conversationLayout.setOnClickListener(v -> {
                FirebaseFirestore.getInstance().collection(StringConstants.KEY_COLLECTIONS_USER)
                        .document(message.getConversationId())
                        .get()
                        .addOnSuccessListener(task -> {
                            if(task != null) {
                                User user = new User();
                                user.setDisplayName(task.getString(StringConstants.KEY_DISPLAY_NAME));
                                user.setUsername(task.getString(StringConstants.KEY_USER_NAME));
                                user.setImage(task.getString(StringConstants.KEY_AVATAR));
                                user.setToken(task.getString(StringConstants.KEY_FCM_TOKEN));
                                user.setUserId(task.getId());
                                listener.onUserClicked(user);
                            }
                        });
            });
        }


    }

}
