package com.aesncast.pw_android;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aesncast.PwCore.PwDomain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DomainItemAdapter
        extends RecyclerView
        .Adapter<DomainItemAdapter.DomainItemViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    private final List<PwDomain> domainList;

    DomainItemAdapter(Collection<PwDomain> domains)
    {
        this.domainList = new ArrayList<>(domains);
    }

    @NonNull
    @Override
    public DomainItemViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.listitem_domain,
                        viewGroup,
                        false);

        return new DomainItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull DomainItemViewHolder parentViewHolder,
            int position)
    {
        PwDomain domainItem = domainList.get(position);

        parentViewHolder.domainNameLabel.setText(domainItem.name);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                parentViewHolder.domainUsersRecyclerViews.getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        layoutManager.setInitialPrefetchItemCount(domainItem.users.size());

        UserItemAdapter childItemAdapter = new UserItemAdapter(domainItem.users.values());

        parentViewHolder.domainUsersRecyclerViews.setLayoutManager(layoutManager);
        parentViewHolder.domainUsersRecyclerViews.setAdapter(childItemAdapter);
        parentViewHolder.domainUsersRecyclerViews.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount()
    {
        return domainList.size();
    }

    static class DomainItemViewHolder
            extends RecyclerView.ViewHolder {

        private final TextView domainNameLabel;
        private final RecyclerView domainUsersRecyclerViews;

        DomainItemViewHolder(final View itemView)
        {
            super(itemView);

            domainNameLabel = itemView.findViewById(R.id.domain_name_label);
            domainUsersRecyclerViews = itemView.findViewById(R.id.user_recyclerview);
        }
    }
}