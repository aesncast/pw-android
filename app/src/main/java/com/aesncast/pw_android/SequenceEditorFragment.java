package com.aesncast.pw_android;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.aesncast.PwCore.Pwfile;
import com.aesncast.PwCore.PwfileParser;
import com.aesncast.PwCore.Sequence;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

        setupReadonly();
    }

    private void setupReadonly() {
        Pwfile instance = PwfileSingleton.instance.get();

        if (!instance.sequences.containsKey(arg_sequenceName))
            return;

        Sequence s = instance.sequences.get(arg_sequenceName);

        if (!s.readonly)
            return;

        sequenceNameEntry.setEnabled(false);
        sequenceCodeEntry.setEnabled(false);
        // technically shouldn't do this, but we have no way of storing default built-in sequences yet
        defaultSwitch.setEnabled(false);
        saveSequenceButton.setEnabled(false);

        /*
        View.OnClickListener showReadonlyToast = (v) -> Toast.makeText(v.getContext(), getString(R.string.sequence_is_readonly), Toast.LENGTH_SHORT).show();
        sequenceNameEntry.setOnClickListener(showReadonlyToast);
        sequenceCodeEntry.setOnClickListener(showReadonlyToast);
        */
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

        if (s.is_default)
        {
            defaultSwitch.setEnabled(false);

            /* can't consume onClick events when view is disabled
            defaultSwitch.setOnClickListener(v ->
                Toast.makeText(v.getContext(), getString(R.string.cannot_remove_only_default_sequence), Toast.LENGTH_SHORT).show()
            );
             */
        }
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
        String seqName = this.sequenceNameEntry.getText().toString();
        String source = this.sequenceCodeEntry.getText().toString();
        boolean def = this.defaultSwitch.isChecked();

        String fullSource = "[" + seqName + "]\n" + String.join("\n    ", source.split("\n"));

        PwfileParser.ParseResult<Sequence> pr = null;

        try {
            pr = PwfileParser.parsePwlist4Sequence(fullSource.split("\n"), 0);
        }
        catch (ParseException e)
        {
            showError(e.getMessage());
            return;
        }

        Sequence seq = pr.result;
        seq.is_default = def;
        Pwfile instance = PwfileSingleton.instance.get();

        instance.sequences.put(seq.name, seq);

        if (seq.is_default) {
            instance.setDefaultSequence(seq.name);
            defaultSwitch.setEnabled(false);
        }

        PwfileSingleton.instance.save();

        Toast.makeText(getContext(), R.string.saved, Toast.LENGTH_SHORT).show();
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

    private void showError(String msg)
    {
        AlertDialog.Builder a = new AlertDialog.Builder(view.getContext());
        a.setTitle(R.string.something_went_wrong);
        a.setMessage(msg);
        a.create().show();
    }
}