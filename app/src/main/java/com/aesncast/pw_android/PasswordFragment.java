package com.aesncast.pw_android;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class PasswordFragment extends Fragment {
    private static final String ARG_DOMAIN = "domain";
    private static final String ARG_USER = "user";
    private static final String ARG_SEQUENCE = "sequence";

    private String arg_domain;
    private String arg_user;
    private String arg_sequence;

    private View view;
    private AutoCompleteTextView domainEntry;
    private AutoCompleteTextView userEntry;
    private EditText keyEntry;
    private AutoCompleteTextView sequenceEntry;
    private Button generatePasswordButton;

    public PasswordFragment() {
    }

    public static PasswordFragment newInstance(String domain, String user, String sequence) {
        PasswordFragment fragment = new PasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOMAIN, domain);
        args.putString(ARG_USER, user);
        args.putString(ARG_SEQUENCE, sequence);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arg_domain = getArguments().getString(ARG_DOMAIN);
            arg_user = getArguments().getString(ARG_USER);
            arg_sequence = getArguments().getString(ARG_SEQUENCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password, container, false);
        setup();
        return view;
    }

    private void setup()
    {
        setupDomainEntry();
        setupUserEntry();
        setupKeyEntry();
        setupSequenceEntry();

        generatePasswordButton = view.findViewById(R.id.generate_button);
        generatePasswordButton.setOnClickListener(v -> this.generatePassword());
    }

    private void setupDomainEntry()
    {
        domainEntry = view.findViewById(R.id.domain_entry);
        domainEntry.setAutofillHints("a", "b", "cde");
    }

    private void setupUserEntry() {
        userEntry = view.findViewById(R.id.user_entry);
    }

    private void setupKeyEntry() {
        keyEntry = view.findViewById(R.id.key_entry);
    }

    private void setupSequenceEntry() {
        sequenceEntry = view.findViewById(R.id.sequence_entry);
    }

    private void generatePassword() {
        String domain = domainEntry.getText().toString();
        String user = userEntry.getText().toString();
        String sequenceName = sequenceEntry.getText().toString();
        String key = keyEntry.getText().toString();

        AlertDialog.Builder a = new AlertDialog.Builder(view.getContext());
        a.setTitle("abc");
        a.setMessage(String.format("%s, %s, %s, %s", key, domain, user, sequenceName));
        a.create().show();
    }

    // TODO: onUserChange: set sequence automatically
}