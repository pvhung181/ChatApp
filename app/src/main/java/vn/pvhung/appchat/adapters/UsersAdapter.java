package vn.pvhung.appchat.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.pvhung.appchat.databinding.ItemUserContainerBinding;
import vn.pvhung.appchat.helpers.ImageHelper;
import vn.pvhung.appchat.listeners.UserListener;
import vn.pvhung.appchat.models.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;
    UserListener userListener;

    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserContainerBinding itemUserContainerBinding = ItemUserContainerBinding
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );
        return new UserViewHolder(itemUserContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position), userListener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private ItemUserContainerBinding binding;

        public UserViewHolder(ItemUserContainerBinding itemUserContainerBinding) {
            super(itemUserContainerBinding.getRoot());
            binding = itemUserContainerBinding;
        }

        public void setUserData(User user, UserListener listener) {
            binding.avatar.setImageBitmap(ImageHelper.decodeImage(user.getImage()));
            binding.displayName.setText(user.getDisplayName());
            binding.userName.setText(String.format("@%s", user.getUsername()));

            if (listener != null)
                binding.userLayout.setOnClickListener(v -> listener.onUserClicked(user));
        }
    }
}
