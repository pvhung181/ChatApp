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
import vn.pvhung.appchat.helpers.ImageHelper;
import vn.pvhung.appchat.models.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users = null;

    public UsersAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_container, parent, false);
        return new UserViewHolder(view);
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
        private final ImageView avatar;
        private final TextView displayName;
        private final TextView username;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            displayName = itemView.findViewById(R.id.display_name);
            username = itemView.findViewById(R.id.user_name);
        }

        public void setUserData(User user) {
            avatar.setImageBitmap(ImageHelper.decodeImage(user.getImage()));
            displayName.setText(user.getName());
            username.setText(user.getEmail());
        }

        public ImageView getAvatar() {
            return avatar;
        }

        public TextView getDisplayName() {
            return displayName;
        }

        public TextView getUsername() {
            return username;
        }
    }
}
