package com.agonzalez.practica3_albertogonzalezhernandez;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OptionsFragment extends Fragment {

    Context context;
    Activity activity;
    Button exportContactsBtn, openScheduleBtn;

    private ContactAppSQLiteDataBase contactsAppDb;

    public OptionsFragment() {
        // Required empty public constructor
    }

    public static OptionsFragment newInstance() {
        OptionsFragment fragment = new OptionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        context = requireContext();
        activity = requireActivity();

        //DB
        contactsAppDb = new ContactAppSQLiteDataBase(context);

        initListenersAndWatchers(view);

        return view;
    }

    private void initListenersAndWatchers(final View view) {
        exportContactsBtn = view.findViewById(R.id.exportToFileBtn);
        openScheduleBtn = view.findViewById(R.id.openScheduleBtn);

        exportContactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportContacts(view);
            }
        });
    }

    private void exportContacts(View view) {
        ExternalStorageSD externalStorageSD = new ExternalStorageSD();

        List<ContactItem> contactsList = contactsAppDb.getContacts();
        externalStorageSD.writeDataOnExternalStorage(contactsList, context, view);
    }

    private String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}