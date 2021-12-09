package com.aesncast.pw_android;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aesncast.PwCore.PwUser;
import com.aesncast.PwCore.Pwfile;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserItemAdapter
    extends RecyclerView
                .Adapter<UserItemAdapter.UserItemViewHolder> {

    public interface EmptyUsersListener {
        void onUsersEmpty(UserItemAdapter adapter);
    }

    private final String domainName;
    private final List<PwUser> userList;
    private EmptyUsersListener listener;
    int position = 0;

    // Constructor
    UserItemAdapter(String domain, Collection<PwUser> users)
    {
        this.domainName = domain;
        this.userList = new ArrayList<>(users);
    }

    UserItemAdapter(String domain, Collection<PwUser> users, EmptyUsersListener listener)
    {
        this(domain, users);
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(
        @NonNull ViewGroup viewGroup,
        int i)
    {
        // Here we inflate the corresponding
        // layout of the child item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                            R.layout.listitem_user,
                            viewGroup,
                            false);

        return new UserItemViewHolder(view, i, this::onUserItemContextMenuClick);
    }

    private void onUserItemContextMenuClick(MenuItem i, UserItemViewHolder viewHolder)
    {
        int id = i.getItemId();

        if (id == R.id.remove_domain_user)
        {
            this.deleteItem(viewHolder.index);
        }
    }

    @Override
    public void onBindViewHolder(
        @NonNull UserItemViewHolder childViewHolder,
        int position)
    {
        PwUser childItem = userList.get(position);

        childViewHolder.userNameLabel.setText(childItem.name);
        childViewHolder.userSequenceLabel.setText(childItem.sequence_name);

        childViewHolder.itemView.setOnClickListener(l -> {
            MainActivity a = (MainActivity)AndroidUtil.getActivity(childViewHolder.itemView);
            a.navigateToPasswordGenerator(domainName, childItem.name, childItem.sequence_name);
        });

        childViewHolder.itemView.setOnLongClickListener(v -> {
            this.position = childViewHolder.getAdapterPosition();
            return false;
        });
    }

    @Override
    public int getItemCount()
    {
        return userList.size();
    }

    public void deleteItem(int position) {
        PwUser removed = userList.remove(position);

        Pwfile instance = PwfileSingleton.instance.get();
        instance.domains.get(domainName).users.remove(removed.name);
        PwfileSingleton.instance.save();

        notifyItemRemoved(position);

        if (userList.isEmpty())
            notifyUsersEmpty();
    }

    private void notifyUsersEmpty() {
        if (this.listener != null)
            this.listener.onUsersEmpty(this);
    }

    public interface UserItemContextMenuItemClickListener
    {
        void onClick(MenuItem i, UserItemViewHolder viewHolder);
    }

    static class UserItemViewHolder
        extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener
    {
        int index;
        UserItemContextMenuItemClickListener clickListener;
        TextView userNameLabel;
        TextView userSequenceLabel;

        UserItemViewHolder(View itemView, int i, UserItemContextMenuItemClickListener listener)
        {
            super(itemView);
            this.index = i;
            this.clickListener = listener;

            userNameLabel = itemView.findViewById(R.id.user_name_label);
            userSequenceLabel = itemView.findViewById(R.id.user_sequence_label);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem i = menu.add(Menu.NONE, R.id.remove_domain_user, Menu.NONE, R.string.remove);
            i.setOnMenuItemClickListener(this::onMenuItemClick);
        }

        private boolean onMenuItemClick(MenuItem i)
        {
            if (this.clickListener != null) {
                this.clickListener.onClick(i, this);
                return true;
            }

            return false;
        }
    }
}