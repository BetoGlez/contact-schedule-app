package com.agonzalez.practica3_albertogonzalezhernandez;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AppointmentCreateEditFragment extends Fragment {

    Context context;
    Activity activity;
    CalendarView appointmentCalendarView;
    TextInputLayout appointmentTitleField;
    Button saveAppointmentBtn, cancelAppointmentBtn;

    private AppointmentItem editableAppointmentInfo;

    public AppointmentCreateEditFragment() {
        // Required empty public constructor
    }

    public static AppointmentCreateEditFragment newInstance(String param1, String param2) {
        AppointmentCreateEditFragment fragment = new AppointmentCreateEditFragment();
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
        View view = inflater.inflate(R.layout.fragment_appointment_create_edit, container, false);

        context = requireContext();
        activity = requireActivity();

        initListenersAndWatchers(view);

        editableAppointmentInfo = getAppointmentInfoData();
        if (!editableAppointmentInfo.getId().trim().isEmpty()) {
            System.out.println("Edition mode for appointment: " + editableAppointmentInfo.getTitle());
            setAppointmentData(editableAppointmentInfo);
        } else {
            System.out.println("Appointment creation mode");
        }

        return view;
    }

    private AppointmentItem getAppointmentInfoData() {
        AppointmentItem appointmentInfo = new AppointmentItem("", "", "");
        Intent i = activity.getIntent();
        if(i.getSerializableExtra("APPOINTMENT_INFO_DATA") != null) {
            appointmentInfo = (AppointmentItem) i.getSerializableExtra("APPOINTMENT_INFO_DATA");
        }
        return appointmentInfo;
    }

    private void initListenersAndWatchers(View view) {
        appointmentCalendarView = view.findViewById(R.id.appointmentCalendarView);
        appointmentTitleField = view.findViewById(R.id.appointmentTitleField);
        saveAppointmentBtn = view.findViewById(R.id.saveAppointmentBtn);
        cancelAppointmentBtn = view.findViewById(R.id.cancelAppointmentBtn);

        appointmentTitleField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNotEmpty(String.valueOf(s), appointmentTitleField);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSaveBtn();
            }
        });

        cancelAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCancelBtn();
            }
        });
    }

    private void clickCancelBtn() {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    private void clickSaveBtn() {
        AppointmentItem savedAppointment = composeAppointment();
        if (!savedAppointment.getId().trim().isEmpty()) {
            Intent i = new Intent();
            i.putExtra("APPOINTMENT_SAVE_DATA", savedAppointment);
            activity.setResult(Activity.RESULT_OK, i);
            activity.finish();
        } else {
            Toast toast = Toast.makeText(context, getResources().getText(R.string.completeFields), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean isNotEmpty(String text, TextInputLayout field) {
        boolean isNotEmpty;
        if(!text.trim().isEmpty()) {
            isNotEmpty = true;
            field.setError(null);
        } else {
            isNotEmpty = false;
            field.setError(getResources().getString(R.string.emptyFieldError));
        }
        return isNotEmpty;
    }

    private AppointmentItem composeAppointment() {
        AppointmentItem appointmentToSave = new AppointmentItem("", "", "");
        String appointmentTitle = appointmentTitleField.getEditText().getText().toString().trim();
        String appointmentDate = "18/02/2021";
        boolean areFieldsComplete = !appointmentTitle.isEmpty() && !appointmentDate.isEmpty();
        if (areFieldsComplete) {
            boolean isErrorInForm = !TextUtils.isEmpty(appointmentTitleField.getError());
            if (!isErrorInForm) {
                String appointmentId = editableAppointmentInfo.getId().trim().isEmpty() ? "-1" : editableAppointmentInfo.getId();
                appointmentToSave = new AppointmentItem(appointmentId, appointmentTitle, appointmentDate);
            }
        }
        return appointmentToSave;
    }

    private void setAppointmentData(AppointmentItem appointmentData) {
        appointmentTitleField.getEditText().setText(appointmentData.getTitle());
        // TODO: Set date
    }
}