package com.aesncast.pw_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aesncast.PwCore.PwUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecentUserItemAdapter
    extends RecyclerView
                .Adapter<RecentUserItemAdapter.UserItemViewHolder> {

    private final List<RecentUserEntry> userList;

    // Constructor
    RecentUserItemAdapter(Collection<RecentUserEntry> users)
    {
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
                            R.layout.listitem_recent_user,
                            viewGroup,
                            false);

        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
        @NonNull UserItemViewHolder childViewHolder,
        int position)
    {
        RecentUserEntry childItem = userList.get(position);

        childViewHolder.userNameLabel.setText(childItem.userName);
        childViewHolder.userDomainLabel.setText(childItem.domainName);
        childViewHolder.userSequenceLabel.setText(childItem.sequenceName);

        childViewHolder.view.setOnClickListener(l -> {
            MainActivity a = (MainActivity)AndroidUtil.getActivity(childViewHolder.view);
            a.navigateToPasswordGenerator(childItem.domainName, childItem.userName, childItem.sequenceName);
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
        TextView userDomainLabel;
        TextView userSequenceLabel;

        UserItemViewHolder(View itemView)
        {
            super(itemView);
            view = itemView;
            userNameLabel = itemView.findViewById(R.id.user_name_label);
            userDomainLabel = itemView.findViewById(R.id.user_domain_label);
            userSequenceLabel = itemView.findViewById(R.id.user_sequence_label);
        }
    }
}