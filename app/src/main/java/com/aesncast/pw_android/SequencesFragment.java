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
import com.aesncast.PwCore.Sequence;

import java.util.ArrayList;
import java.util.List;

public class SequencesFragment extends Fragment {
    private View view;
    private TextView emptyText;

    public SequencesFragment() {
    }

    public static SequencesFragment newInstance() {
        SequencesFragment fragment = new SequencesFragment();
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
        view = inflater.inflate(R.layout.fragment_sequences, container, false);
        setup();
        return view;
    }

    private void setup() {
        emptyText = view.findViewById(R.id.emptyText);

        setupSequenceList();
    }

    private void setupSequenceList() {
        Pwfile instance = PwfileSingleton.instance.get();
        List<Sequence> sequences = new ArrayList<>(instance.sequences.values());

        RecyclerView sequenceRecyclerview = view.findViewById(R.id.sequence_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        SequenceItemAdapter adapter = new SequenceItemAdapter(sequences);

        sequenceRecyclerview.setAdapter(adapter);
        sequenceRecyclerview.setLayoutManager(layoutManager);

        if (sequences.isEmpty())
            emptyText.setVisibility(View.VISIBLE);
        else
            emptyText.setVisibility(View.GONE);
    }
}