package com.aesncast.pw_android;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aesncast.PwCore.Pwfile;
import com.aesncast.PwCore.Sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SequenceItemAdapter
        extends RecyclerView.Adapter<SequenceItemAdapter.SequenceViewHolder> {

    private final List<Sequence> sequences;
    public int position;

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

        if (childItem.is_default && !childItem.readonly)
            childViewHolder.sequenceNameLabel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        else if (!childItem.is_default && childItem.readonly)
            childViewHolder.sequenceNameLabel.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        else if (childItem.is_default && childItem.readonly)
            childViewHolder.sequenceNameLabel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));

        childViewHolder.view.setOnClickListener(l -> {
            MainActivity a = (MainActivity)AndroidUtil.getActivity(childViewHolder.view);
            a.navigateToSequenceEditor(childItem.name);
        });

        childViewHolder.itemView.setOnLongClickListener(v -> {
            this.position = childViewHolder.getAdapterPosition();
            return false;
        });
    }

    @Override
    public int getItemCount()
    {
        return sequences.size();
    }

    public void deleteItem(int position) throws ReadonlySequenceException, DefaultSequenceException {
        Sequence seq = sequences.get(position);

        if (seq == null)
            return;

        if (seq.is_default)
            throw new DefaultSequenceException();

        else if (seq.readonly)
            throw new ReadonlySequenceException();

        Sequence removed = sequences.remove(position);

        Pwfile instance = PwfileSingleton.instance.get();
        instance.sequences.remove(removed.name);
        PwfileSingleton.instance.save();

        notifyItemRemoved(position);
    }

    static class SequenceViewHolder
            extends RecyclerView.ViewHolder
    implements View.OnCreateContextMenuListener
    {

        View view;
        TextView sequenceNameLabel;

        SequenceViewHolder(View itemView)
        {
            super(itemView);
            view = itemView;
            sequenceNameLabel = itemView.findViewById(R.id.sequence_name_label);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // menu.add(Menu.NONE, R.id.remove, Menu.NONE, R.string.remove);
        }
    }

    public static class DefaultSequenceException extends Exception
    {
        public DefaultSequenceException()
        {
            super();
        }

        public DefaultSequenceException(String message)
        {
            super(message);
        }
    }

    public static class ReadonlySequenceException extends Exception
    {
        public ReadonlySequenceException()
        {
            super();
        }

        public ReadonlySequenceException(String message)
        {
            super(message);
        }
    }
}
