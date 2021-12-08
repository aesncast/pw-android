package com.aesncast.pw_android;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aesncast.PwCore.PwUser;
import com.aesncast.PwCore.Pwfile;
import com.aesncast.PwCore.Sequence;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserItemAdapter
    extends RecyclerView
                .Adapter<UserItemAdapter.UserItemViewHolder> {

    private final String domainName;
    private final List<PwUser> userList;
    int position = 0;

    // Constructor
    UserItemAdapter(String domain, Collection<PwUser> users)
    {
        this.domainName = domain;
        this.userList = new ArrayList<>(users);
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

        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
        @NonNull UserItemViewHolder childViewHolder,
        int position)
    {
        PwUser childItem = userList.get(position);

        childViewHolder.userNameLabel.setText(childItem.name);
        childViewHolder.userSequenceLabel.setText(childItem.sequence_name);

        childViewHolder.view.setOnClickListener(l -> {
            MainActivity a = (MainActivity)AndroidUtil.getActivity(childViewHolder.view);
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
    }

    static class UserItemViewHolder
        extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener
    {

        View view;
        TextView userNameLabel;
        TextView userSequenceLabel;

        UserItemViewHolder(View itemView)
        {
            super(itemView);
            view = itemView;
            userNameLabel = itemView.findViewById(R.id.user_name_label);
            userSequenceLabel = itemView.findViewById(R.id.user_sequence_label);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        }
    }
}