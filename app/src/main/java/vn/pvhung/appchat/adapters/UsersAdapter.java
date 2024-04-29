package vn.pvhung.appchat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.pvhung.appchat.R;
import vn.pvhung.appchat.databinding.ItemUserContainerBinding;
import vn.pvhung.appchat.helpers.ImageHelper;
import vn.pvhung.appchat.models.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;

    public UsersAdapter(List<User> users) {
        this.users = users;
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
        holder.setUserData(users.get(position));
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

        public void setUserData(User user) {
            binding.avatar.setImageBitmap(ImageHelper.decodeImage(user.getImage()));
            binding.displayName.setText(user.getDisplayName());
            binding.userName.setText(String.format("@%s", user.getUsername()));
        }
    }
}
