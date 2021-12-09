package com.aesncast.pw_android;

import android.app.AlertDialog;
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
import com.aesncast.PwCore.Sequence;

import java.util.ArrayList;
import java.util.List;

public class SequencesFragment extends Fragment {
    private View view;
    private TextView emptyText;
    private RecyclerView sequenceRecyclerView;
    private SequenceItemAdapter adapter;

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

        sequenceRecyclerView = view.findViewById(R.id.sequence_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        adapter = new SequenceItemAdapter(sequences);

        sequenceRecyclerView.setAdapter(adapter);
        sequenceRecyclerView.setLayoutManager(layoutManager);

        if (sequences.isEmpty())
            emptyText.setVisibility(View.VISIBLE);
        else
            emptyText.setVisibility(View.GONE);

        // context menu
        registerForContextMenu(sequenceRecyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.sequence_recyclerview) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.sequences_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.remove_sequence) {
            int pos = ((SequenceItemAdapter)sequenceRecyclerView.getAdapter()).position;
            try {
                adapter.deleteItem(pos);
            } catch (SequenceItemAdapter.DefaultSequenceException e) {
                showError(getString(R.string.cannot_remove_default_sequence));
                return true;
            } catch (SequenceItemAdapter.ReadonlySequenceException e) {
                showError(getString(R.string.cannot_remove_readonly_sequence));
                return true;
            }
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
}