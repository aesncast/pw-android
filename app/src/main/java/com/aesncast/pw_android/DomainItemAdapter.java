package com.aesncast.pw_android;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aesncast.PwCore.PwDomain;
import com.aesncast.PwCore.PwUser;
import com.aesncast.PwCore.Pwfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DomainItemAdapter
        extends RecyclerView
        .Adapter<DomainItemAdapter.DomainItemViewHolder> {

    public interface DomainItemAddedListener
    {
        void onAdd(DomainItemViewHolder viewHolder);
    }

    public interface DomainEmptyCallback
    {
        void confirmDeletion(int position);
    }

    public interface DomainEmptyListener
    {
        void onDomainEmpty(DomainEmptyCallback callback, String domainName, int position);
    }

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private final List<PwDomain> domainList;
    private DomainItemAddedListener onDomainAddedListener;
    private DomainEmptyListener onDomainEmptyListener;


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

    private void notifyOnDomainAddedListener(DomainItemViewHolder viewHolder) {
        if (this.onDomainAddedListener != null)
            this.onDomainAddedListener.onAdd(viewHolder);
    }

    public void setOnDomainItemAddedListener(DomainItemAddedListener listener)
    {
        this.onDomainAddedListener = listener;
    }

    public void setOnDomainEmptyListener(DomainEmptyListener listener)
    {
        this.onDomainEmptyListener = listener;
    }

    @Override
    public void onBindViewHolder(
            @NonNull DomainItemViewHolder parentViewHolder,
            int position)
    {
        PwDomain domainItem = domainList.get(position);

        parentViewHolder.domainNameLabel.setText(domainItem.name);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                parentViewHolder.domainUsersRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        layoutManager.setInitialPrefetchItemCount(domainItem.users.size());

        UserItemAdapter childItemAdapter = new UserItemAdapter(domainItem.name, domainItem.users.values(), x -> this.onUsersEmpty(position));

        parentViewHolder.domainUsersRecyclerView.setLayoutManager(layoutManager);
        parentViewHolder.domainUsersRecyclerView.setAdapter(childItemAdapter);
        parentViewHolder.domainUsersRecyclerView.setRecycledViewPool(viewPool);

        parentViewHolder.domainNameLabel.setOnClickListener(l -> {
            MainActivity a = (MainActivity)AndroidUtil.getActivity(parentViewHolder.itemView);
            a.navigateToPasswordGenerator(domainItem.name, "", "");
        });

        notifyOnDomainAddedListener(parentViewHolder);

        /*
        parentViewHolder.domainUsersRecyclerView.setOnLongClickListener(v -> {
            int index = parentViewHolder.getAdapterPosition();
            System.out.format("Selected domain %s at index %d\n", domainItem.name, index);
            this.position = index;
            return false;
        });
         */
    }

    private void onUsersEmpty(int position)
    {
        try {
            PwDomain item = domainList.get(position);
            this.onDomainEmptyListener.onDomainEmpty(this::deleteItem, item.name, position);
        }
        catch (IndexOutOfBoundsException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void deleteItem(int position) {
        PwDomain removed = domainList.remove(position);

        Pwfile instance = PwfileSingleton.instance.get();
        instance.domains.remove(removed.name);
        PwfileSingleton.instance.save();

        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount()
    {
        return domainList.size();
    }

    static class DomainItemViewHolder
            extends RecyclerView.ViewHolder {

        public final TextView domainNameLabel;
        public final RecyclerView domainUsersRecyclerView;

        DomainItemViewHolder(final View itemView)
        {
            super(itemView);

            domainNameLabel = itemView.findViewById(R.id.domain_name_label);
            domainUsersRecyclerView = itemView.findViewById(R.id.user_recyclerview);
        }
    }
}