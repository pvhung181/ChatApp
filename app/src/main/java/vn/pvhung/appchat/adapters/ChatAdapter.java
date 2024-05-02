package vn.pvhung.appchat.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.pvhung.appchat.databinding.ItemContainerReceivedMessageBinding;
import vn.pvhung.appchat.databinding.ItemContainerSentMessageBinding;
import vn.pvhung.appchat.models.ChatMessage;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ChatMessage> messages;
    private Bitmap receiverAvatar;
    String senderId;
    private static final int SENT_VIEW_TYPE = 0;
    private static final int RECEIVED_VIEW_TYPE = 1;

    public ChatAdapter(List<ChatMessage> messages, String senderId, Bitmap receiverAvatar) {
        this.messages = messages;
        this.receiverAvatar = receiverAvatar;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENT_VIEW_TYPE) {
            ItemContainerSentMessageBinding s = ItemContainerSentMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    );
            return new SentMessageViewHolder(s);
        }else {
            ItemContainerReceivedMessageBinding r = ItemContainerReceivedMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    );
            return new ReceivedMessageViewHolder(r);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == SENT_VIEW_TYPE)  ((SentMessageViewHolder) holder).setData(messages.get(position));
        else  ((ReceivedMessageViewHolder) holder).setData(messages.get(position), receiverAvatar);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getSenderId().equals(senderId)) return SENT_VIEW_TYPE;
        else return RECEIVED_VIEW_TYPE;
    }

    private static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        ItemContainerSentMessageBinding binding;

        public SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage m) {
            binding.textMessage.setText(m.getMessage());
            binding.textDatetime.setText(m.getDatetime());
//            binding.textMessage.setLongClickable(true);
//            binding.textMessage.setOnLongClickListener(v -> {
//
//            });
        }

    }

    private static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        ItemContainerReceivedMessageBinding binding;

        public ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(ChatMessage m, Bitmap avatar) {
            binding.textMessage.setText(m.getMessage());
            binding.textDatetime.setText(m.getDatetime());
            binding.avatar.setImageBitmap(avatar);
        }
    }
}
