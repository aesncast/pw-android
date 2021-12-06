package com.aesncast.pw_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aesncast.PwCore.PwUser;

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

        return new UserItemViewHolder(view, domainName);
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
    }

    @Override
    public int getItemCount()
    {
        return userList.size();
    }

    static class UserItemViewHolder
        extends RecyclerView.ViewHolder {

        View view;
        TextView userNameLabel;
        TextView userSequenceLabel;

        UserItemViewHolder(View itemView, String domainName)
        {
            super(itemView);
            view = itemView;
            userNameLabel = itemView.findViewById(R.id.user_name_label);
            userSequenceLabel = itemView.findViewById(R.id.user_sequence_label);
        }
    }
}