package com.aesncast.pw_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aesncast.PwCore.Pwfile;

public class DomainsUsersFragment extends Fragment {
    private View view;
    private TextView emptyText;

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

        RecyclerView domainRecyclerView = view.findViewById(R.id.domain_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        DomainItemAdapter domainItemAdapter = new DomainItemAdapter(instance.domains.values());

        domainRecyclerView.setAdapter(domainItemAdapter);
        domainRecyclerView.setLayoutManager(layoutManager);

        if (instance.domains.isEmpty())
            emptyText.setVisibility(View.VISIBLE);
        else
            emptyText.setVisibility(View.GONE);
    }
}