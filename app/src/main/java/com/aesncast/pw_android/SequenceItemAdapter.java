package com.aesncast.pw_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aesncast.PwCore.Sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SequenceItemAdapter
        extends RecyclerView.Adapter<SequenceItemAdapter.SequenceViewHolder> {

    private final List<Sequence> sequences;

    public SequenceItemAdapter(Collection<Sequence> sequences) {
        this.sequences = new ArrayList<>(sequences);
    }

    @NonNull
    @Override
    public SequenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listitem_sequence,
                parent,
                false);

        return new SequenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SequenceViewHolder childViewHolder,
            int position)
    {
        Sequence childItem = sequences.get(position);

        childViewHolder.sequenceNameLabel.setText(childItem.name);

        childViewHolder.view.setOnClickListener(l -> {
            MainActivity a = (MainActivity)AndroidUtil.getActivity(childViewHolder.view);
            a.navigateToSequenceEditor(childItem.name);
        });
    }

    @Override
    public int getItemCount()
    {
        return sequences.size();
    }

    static class SequenceViewHolder
            extends RecyclerView.ViewHolder {

        View view;
        TextView sequenceNameLabel;

        SequenceViewHolder(View itemView)
        {
            super(itemView);
            view = itemView;
            sequenceNameLabel = itemView.findViewById(R.id.sequence_name_label);
        }
    }
}
