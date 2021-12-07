package com.aesncast.pw_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.aesncast.PwCore.Pwfile;
import com.aesncast.PwCore.Sequence;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SequenceEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SequenceEditorFragment extends Fragment {
    private static final int THRESHOLD = 0;
    private static final String ARG_SEQUENCE_NAME = "";

    private String arg_sequenceName;

    private View view;
    private AutoCompleteTextView sequenceNameEntry;
    private EditText sequenceCodeEntry;
    private Button saveSequenceButton;
    private Switch defaultSwitch;

    public SequenceEditorFragment() {
    }

    public static SequenceEditorFragment newInstance(String sequenceName) {
        SequenceEditorFragment fragment = new SequenceEditorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEQUENCE_NAME, sequenceName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arg_sequenceName = getArguments().getString(ARG_SEQUENCE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sequence_editor, container, false);
        setup();
        return view;
    }

    private void setup() {
        setupSequenceNameEntry();
        setupSequenceCodeEntry();
        setupDefaultSwitch();

        saveSequenceButton = view.findViewById(R.id.sequence_save_button);
        saveSequenceButton.setOnClickListener(v -> this.saveSequence());
    }

    private void setupDefaultSwitch() {
        defaultSwitch = view.findViewById(R.id.sequence_default_switch);

        if (arg_sequenceName.isEmpty())
            return;

        Pwfile instance = PwfileSingleton.instance.get();

        if (!instance.sequences.containsKey(arg_sequenceName))
            return;

        Sequence s = instance.sequences.get(arg_sequenceName);

        defaultSwitch.setChecked(s.is_default);
    }

    private void setupSequenceCodeEntry() {
        sequenceCodeEntry = view.findViewById(R.id.sequence_code_entry);

        if (arg_sequenceName.isEmpty())
            return;

        Pwfile instance = PwfileSingleton.instance.get();

        if (!instance.sequences.containsKey(arg_sequenceName))
            return;

        Sequence s = instance.sequences.get(arg_sequenceName);

        String body = String.join("\n", s.segments.stream().map(x -> x.toString()).toArray(String[]::new));
        sequenceCodeEntry.setText(body);
    }

    private void saveSequence() {
        // TODO: implement
        String seqName = this.sequenceNameEntry.getText().toString();
        String source = this.sequenceCodeEntry.getText().toString();
        System.out.format("%s: %s\n", seqName, source);
    }

    private void setupSequenceNameEntry() {
        sequenceNameEntry = view.findViewById(R.id.sequence_name_entry);
        sequenceNameEntry.setThreshold(THRESHOLD);

        if (!arg_sequenceName.isEmpty())
            sequenceNameEntry.setText(arg_sequenceName);

        updateSequenceSuggestions();
    }

    private void updateSequenceSuggestions() {
        if (sequenceNameEntry == null)
            return;

        Pwfile instance = PwfileSingleton.instance.get();

        List<String> sequences = new ArrayList<>(instance.sequences.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, sequences.toArray(new String[]{}));
        sequenceNameEntry.setAdapter(adapter);
    }
}