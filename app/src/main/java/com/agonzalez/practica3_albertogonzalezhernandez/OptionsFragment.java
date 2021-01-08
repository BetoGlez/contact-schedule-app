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

        initListenersAndWatchers(view);

        return view;
    }

    private void initListenersAndWatchers(View view) {
        exportContactsBtn = view.findViewById(R.id.exportToFileBtn);
        openScheduleBtn = view.findViewById(R.id.openScheduleBtn);

        exportContactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportContacts();
            }
        });
    }

    private void exportContacts() {
        ExternalStorageSD externalStorageSD = new ExternalStorageSD();

        List<ContactItem> contactsList = new ArrayList<>();

        contactsList.add(new ContactItem(getUniqueId(), "Alberto González Hernández", "Calle Obertura 1, 5A. Madrid, Spain 28011", "634270451", "934563284", "betoglez.1902@gmail.com", 1));
        contactsList.add(new ContactItem(getUniqueId(), "Rafael Nadal", "Pascual Yunquera 82. Almuñécar, Granada 18690", "633524395", "987453211", "rnadal@gmail.com", 2));
        contactsList.add(new ContactItem(getUniqueId(), "Lionel Messi", "Cartagena 71. Abanilla, Murcia 30640", "9336482643", "933847321", "lmessi@icloud.com", 3));
        contactsList.add(new ContactItem(getUniqueId(), "Cristiano Ronaldo", "21 Main Street, Egginton 28013", "634527595", "933273842", "cr7@outlook.com", 4));
        contactsList.add(new ContactItem(getUniqueId(), "Jordi Alba", "3 The Ellers, 40 Leeds And Bradford Road, Leeds 28011", "933465721", "988736421", "jalba@gmail.com", 5));
        contactsList.add(new ContactItem(getUniqueId(), "Antoine Griezman", "Flat 1, Shearwater House, 79 Millward Drive, Bletchley 62564", "634526399", "933478900", "agriezman9@hotmail.com", 1));
        contactsList.add(new ContactItem(getUniqueId(), "Novak Djockovic", "Kingscote, Staples Barn Lane, Henfield 29876", "634521211", "748562453", "noledjocko@yahoo.com", 2));
        contactsList.add(new ContactItem(getUniqueId(), "Andres Iniesta", "Hill House, Great Whittington 23412", "635221381", "75634123", "ainiesta@gmail.com", 3));
        contactsList.add(new ContactItem(getUniqueId(), "Javier Hernández", "36 Grampian Way, Eastham 92564", "7772202085", "7772202085", "jhernandez14@yahoo.com.mx", 4));
        contactsList.add(new ContactItem(getUniqueId(), "Rafael Márquez", "39 Warren Close, Hay On Wye 27016", "7771104110", "7772560692", "rmarquez4@gmail.com", 5));

        // TODO: Get contacts from local storage
        externalStorageSD.writeDataOnExternalStorage(contactsList);
    }

    private String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}