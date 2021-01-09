package com.agonzalez.practica3_albertogonzalezhernandez;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoMessageFragment extends Fragment {

    public InfoMessageFragment() {
        // Required empty public constructor
    }

    public static InfoMessageFragment newInstance(String param1, String param2) {
        InfoMessageFragment fragment = new InfoMessageFragment();
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
        return inflater.inflate(R.layout.fragment_info_message, container, false);
    }
}