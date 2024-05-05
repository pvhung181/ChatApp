package vn.pvhung.appchat.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.pvhung.appchat.databinding.ItemConversationContainerBinding;
import vn.pvhung.appchat.helpers.ImageHelper;
import vn.pvhung.appchat.models.ChatMessage;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.RecentConversationViewHolder> {

    private final List<ChatMessage> chatMessages;

    public RecentConversationAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
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
        holder.setData(chatMessages.get(position));
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

        public void setData(ChatMessage message) {
            binding.avatar.setImageBitmap(ImageHelper.decodeImage(message.getConversationImage()));
            binding.displayName.setText(message.getConversationName());
            binding.lastMessage.setText(message.getMessage());
        }


    }

}
