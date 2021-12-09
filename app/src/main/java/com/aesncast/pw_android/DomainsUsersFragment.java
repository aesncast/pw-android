package com.aesncast.pw_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.aesncast.PwCore.Pwfile;

public class DomainsUsersFragment extends Fragment {
    private View view;
    private TextView emptyText;
    RecyclerView domainRecyclerView;
    DomainItemAdapter domainItemAdapter;

    public DomainsUsersFragment() {
    }

    public static DomainsUsersFragment newInstance() {
        DomainsUsersFragment fragment = new DomainsUsersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_domains_users, container, false);
        setup();
        return view;
    }

    private void setup() {
        emptyText = view.findViewById(R.id.emptyText);

        setupDomainList();
    }

    private void setupDomainList()
    {
        Pwfile instance = PwfileSingleton.instance.get();

        domainRecyclerView = view.findViewById(R.id.domain_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        domainItemAdapter = new DomainItemAdapter(instance.domains.values());

        domainRecyclerView.setAdapter(domainItemAdapter);
        domainRecyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(domainRecyclerView);
        domainItemAdapter.setOnDomainItemAddedListener(this::onDomainItemCreated);
        domainItemAdapter.setOnDomainEmptyListener(this::onDomainEmpty);

        if (instance.domains.isEmpty())
            emptyText.setVisibility(View.VISIBLE);
        else
            emptyText.setVisibility(View.GONE);
    }

    private void onDomainEmpty(DomainItemAdapter.DomainEmptyCallback domainEmptyCallback, String domainName, int i) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
        alert.setTitle(String.format(getString(R.string.domain_is_empty), domainName));
        alert.setMessage(R.string.delete_empty_domain);
        alert.setPositiveButton(R.string.yes, (dialog, which) -> {
            domainEmptyCallback.confirmDeletion(i);
            dialog.dismiss();
        });

        alert.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    private void onDomainItemCreated(DomainItemAdapter.DomainItemViewHolder viewHolder)
    {
        registerForContextMenu(viewHolder.domainUsersRecyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.user_recyclerview) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.domains_and_users_context_menu, menu);
        }
    }

    /*
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remove_domain_user) {

            DomainItemAdapter adapter = (DomainItemAdapter)domainRecyclerView.getAdapter();
            DomainItemAdapter.DomainItemViewHolder vh = (DomainItemAdapter.DomainItemViewHolder)domainRecyclerView.findViewHolderForAdapterPosition(adapter.position);
            UserItemAdapter userItemAdapter = (UserItemAdapter) vh.domainUsersRecyclerView.getAdapter();
            userItemAdapter.deleteItem(userItemAdapter.position);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void showError(String msg)
    {
        AlertDialog.Builder a = new AlertDialog.Builder(view.getContext());
        a.setTitle(R.string.something_went_wrong);
        a.setMessage(msg);
        a.create().show();
    }
     */
}