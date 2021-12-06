package com.aesncast.pw_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aesncast.PwCore.Pwfile;

import java.util.List;

public class RecentPasswordsFragment extends Fragment {
    private View view;
    private TextView emptyText;

    public RecentPasswordsFragment() {
    }

    public static RecentPasswordsFragment newInstance() {
        RecentPasswordsFragment fragment = new RecentPasswordsFragment();
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
        view = inflater.inflate(R.layout.fragment_recent_passwords, container, false);
        setup();
        return view;
    }

    private void setup() {
        emptyText = view.findViewById(R.id.emptyText);

        setupRecentUserList();
    }

    private void setupRecentUserList()
    {
        PwPreferences prefs = new PwPreferences(view.getContext());
        List<RecentUserEntry> usrs = prefs.getRecentUsers();

        RecyclerView recentUsersRecyclerView = view.findViewById(R.id.recent_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        RecentUserItemAdapter adapter = new RecentUserItemAdapter(usrs);

        recentUsersRecyclerView.setAdapter(adapter);
        recentUsersRecyclerView.setLayoutManager(layoutManager);

        if (usrs.isEmpty())
            emptyText.setVisibility(View.VISIBLE);
        else
            emptyText.setVisibility(View.GONE);
    }
}