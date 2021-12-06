package com.aesncast.pw_android;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.aesncast.PwCore.PwDomain;
import com.aesncast.PwCore.PwUser;
import com.aesncast.PwCore.Pwfile;
import com.aesncast.PwCore.Sequence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PasswordFragment extends Fragment {
    private static final int THRESHOLD = 0;
    private static final String ARG_DOMAIN = "domain";
    private static final String ARG_USER = "user";
    private static final String ARG_SEQUENCE = "sequence";

    private String arg_domain = "";
    private String arg_user = "";
    private String arg_sequence = "";

    private View view;
    private AutoCompleteTextView domainEntry;
    private AutoCompleteTextView userEntry;
    private EditText keyEntry;
    private AutoCompleteTextView sequenceEntry;
    private Button generatePasswordButton;
    private Button passwordLabel;
    private String password;
    private boolean password_visible = false;

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

        passwordLabel = view.findViewById(R.id.password_label);
        passwordLabel.setOnClickListener(v -> this.togglePassword());
    }

    private void setupDomainEntry()
    {
        domainEntry = view.findViewById(R.id.domain_entry);
        domainEntry.setThreshold(THRESHOLD);

        if (!arg_domain.isEmpty())
            domainEntry.setText(arg_domain);

        updateDomainSuggestions();

        domainEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUserSuggestions();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {
            }
        });
    }

    private void updateDomainSuggestions()
    {
        if (domainEntry == null)
            return;

        List<String> domains = new ArrayList<>();

        if (userEntry == null || userEntry.getText().toString().isEmpty())
        {
            domains = new ArrayList<>(PwfileSingleton.instance.get().domains.keySet());
        }
        else
        {
            String user = userEntry.getText().toString();

            for (PwDomain d : PwfileSingleton.instance.get().domains.values())
                if (d.users.containsKey(user))
                    domains.add(d.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, domains.toArray(new String[]{}));
        domainEntry.setAdapter(adapter);
    }

    private void setupUserEntry() {
        userEntry = view.findViewById(R.id.user_entry);
        userEntry.setThreshold(THRESHOLD);

        if (!arg_user.isEmpty())
            userEntry.setText(arg_user);

        updateUserSuggestions();

        userEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateDomainSuggestions();
                setDomainToUsersDefault();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {
            }
        });
    }

    private void setDomainToUsersDefault()
    {
        String domain = domainEntry.getText().toString();
        String user = userEntry.getText().toString();
        Pwfile instance = PwfileSingleton.instance.get();

        if (!instance.domains.containsKey(domain) || !instance.domains.get(domain).users.containsKey(user))
            return;

        sequenceEntry.setText(instance.domains.get(domain).users.get(user).sequence_name);
    }

    private void updateUserSuggestions()
    {
        if (userEntry == null)
            return;

        List<String> users = new ArrayList<>();
        Pwfile instance = PwfileSingleton.instance.get();

        if (domainEntry == null || domainEntry.getText().toString().isEmpty())
        {
            for (PwDomain d : instance.domains.values())
                for (PwUser u : d.users.values())
                    users.add(u.name);

            users = new ArrayList<>(new HashSet<>(users));
        }
        else
        {
            String domain = domainEntry.getText().toString();

            if (instance.domains.containsKey(domain))
                for (PwUser u : instance.domains.get(domain).users.values())
                    users.add(u.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, users.toArray(new String[]{}));
        userEntry.setAdapter(adapter);
    }

    private void setupKeyEntry() {
        keyEntry = view.findViewById(R.id.key_entry);
    }

    private void setupSequenceEntry() {
        sequenceEntry = view.findViewById(R.id.sequence_entry);
        sequenceEntry.setThreshold(THRESHOLD);

        if (!arg_sequence.isEmpty())
            sequenceEntry.setText(arg_sequence);

        updateSequenceSuggestions();
    }

    private void updateSequenceSuggestions() {
        if (sequenceEntry == null)
            return;

        List<String> sequences = new ArrayList<>();
        Pwfile instance = PwfileSingleton.instance.get();

        sequences.addAll(instance.sequences.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, sequences.toArray(new String[]{}));
        sequenceEntry.setAdapter(adapter);
    }

    private void generatePassword() {
        String domain = domainEntry.getText().toString();
        String user = userEntry.getText().toString();
        String sequenceName = sequenceEntry.getText().toString();
        String key = keyEntry.getText().toString();
        Pwfile instance = PwfileSingleton.instance.get();

        if (sequenceName.isEmpty())
            sequenceName = "DEFAULT";

        Sequence s = instance.getSequence(sequenceName);

        if (s == null)
        {
            showError(String.format("Sequence '%s' not found", sequenceName));
            passwordLabel.setVisibility(View.GONE);
            passwordLabel.setEnabled(false);
            return;
        }

        try
        {
            password = s.execute(key, domain, user);
        }
        catch (Exception e)
        {
            showError(e.getMessage());
            passwordLabel.setVisibility(View.GONE);
            passwordLabel.setEnabled(false);
            return;
        }

        updatePwfile(domain, user, s.name);
        updateUserSuggestions();
        updateDomainSuggestions();

        passwordLabel.setVisibility(View.VISIBLE);
        passwordLabel.setEnabled(true);
        hidePassword();
        // TODO: copy to clipboard
    }

    private void updatePwfile(String domain, String user, String sequenceName) {
        Pwfile instance = PwfileSingleton.instance.get();

        PwDomain dom = null;

        if (!instance.domains.containsKey(domain)) {
            dom = new PwDomain(domain);
            instance.domains.put(domain, dom);
        }
        else
            dom = instance.domains.get(domain);

        PwUser usr = null;

        if (!dom.users.containsKey(user))
        {
            usr = new PwUser(user, sequenceName);
            dom.users.put(user, usr);
        }
        else
            usr = dom.users.get(user);

        Sequence s = instance.getSequence(sequenceName);
        usr.sequence_name = s.name;

        PwfileSingleton.instance.save();
    }

    private void togglePassword() {
        if (password_visible)
            showPassword();
        else
            hidePassword();
    }

    private void hidePassword()
    {
        password_visible = true;
        passwordLabel.setText(R.string.password_copied_tap_to_show);
    }

    private void showPassword()
    {
        password_visible = false;
        passwordLabel.setText(new String(password));
    }

    private void showError(String msg)
    {
        AlertDialog.Builder a = new AlertDialog.Builder(view.getContext());
        a.setTitle(R.string.something_went_wrong);
        a.setMessage(msg);
        a.create().show();
    }
}