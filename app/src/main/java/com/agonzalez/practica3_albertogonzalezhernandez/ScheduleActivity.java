package com.agonzalez.practica3_albertogonzalezhernandez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScheduleActivity extends AppCompatActivity {

    private ContactAppSQLiteDataBase contactsAppDb;
    private List<AppointmentItem> appointmentsList = new ArrayList<>();
    private RecyclerView scheduleRecyclerView;
    private ScheduleAdapter scheduleAdapter;
    private int appointmentPos = 0;

    // Fab
    private FloatingActionButton newAppointmentFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //DB
        contactsAppDb = new ContactAppSQLiteDataBase(getApplicationContext());
        appointmentsList = contactsAppDb.getSchedule();

        // FAB
        newAppointmentFab = findViewById(R.id.newAppointmentFab);
        newAppointmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateAppointmentDetail();
            }
        });

        // Recycler View
        loadScheduleAdapter();
        setScheduleAdapter();
        registerForContextMenu(scheduleRecyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle(R.string.scheduleListMenuHeader);
        inflater.inflate(R.menu.menu_list_actions_recview, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteOption:
                deleteScheduleItem(appointmentPos);
                return true;
            case R.id.detailsOption:
                AppointmentItem selectedAppointment = appointmentsList.get(appointmentPos);
                Intent appointmentInfoIntent = new Intent(this, AppointmentDetailActivity.class);
                appointmentInfoIntent.putExtra("APPOINTMENT_INFO_DATA", selectedAppointment);
                startActivityForResult(appointmentInfoIntent, 300);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadScheduleAdapter() {
        scheduleRecyclerView = findViewById(R.id.scheduleRecycler);
        scheduleAdapter = new ScheduleAdapter(appointmentsList, this);
    }

    private void setScheduleAdapter() {
        scheduleAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentPos = scheduleRecyclerView.getChildAdapterPosition(v);
                openContextMenu(scheduleRecyclerView);
            }
        });

        scheduleRecyclerView.setAdapter(scheduleAdapter);
        scheduleRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void insertScheduleItem(AppointmentItem appointment) {
        String appointmentTitle = appointment.getTitle();
        int newAppointmentId = contactsAppDb.insertAppointment(appointment);
        if (newAppointmentId > -1) {
            appointment.setId(String.valueOf(newAppointmentId));
            appointmentsList.add(appointment);
            scheduleAdapter.notifyItemInserted(appointmentsList.size() - 1);
            String insertAppointmentFeedback = getResources().getString(R.string.insertAppointmentFeedback);
            Snackbar.make(scheduleRecyclerView, appointmentTitle + " " + insertAppointmentFeedback, Snackbar.LENGTH_LONG).show();
        }
    }

    private void editScheduleItem(AppointmentItem appointmentItem) {
        int pos = -1;
        AppointmentItem editedAppointment = new AppointmentItem("", "", "");
        for (AppointmentItem appointmentToSearch: appointmentsList) {
            if (appointmentToSearch.getId().equals(appointmentItem.getId())) {
                editedAppointment = appointmentToSearch;
            }
        }
        if (!editedAppointment.getId().trim().isEmpty()) {
            pos = appointmentsList.indexOf(editedAppointment);
            if (pos > -1) {
                contactsAppDb.editAppointment(appointmentItem);
                appointmentsList.set(pos, appointmentItem);
                scheduleAdapter.notifyItemChanged(pos, appointmentItem);
                String editAppointmentFeedback = getResources().getString(R.string.editAppointmentFeedback);
                Snackbar.make(scheduleRecyclerView, editAppointmentFeedback, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void deleteScheduleItem(int pos){
        String appointmentTitle = appointmentsList.get(pos).getTitle();
        contactsAppDb.deleteAppointment(appointmentsList.get(pos).getId());
        appointmentsList.remove(pos);
        scheduleAdapter.notifyItemRemoved(pos);
        String deleteFeedback = getResources().getString(R.string.deletedFeedback);
        Snackbar.make(scheduleRecyclerView, appointmentTitle + " " + deleteFeedback, Snackbar.LENGTH_LONG).show();
    }

    // Page navigation
    private void navigateAppointmentDetail() {
        Intent appointmentDetailIntent = new Intent(this, AppointmentDetailActivity.class);
        startActivityForResult(appointmentDetailIntent, 301);
    }

    protected void onActivityResult(int code, int result, Intent data) {
        super.onActivityResult(code, result, data);
        System.out.println("Intent response code: " + code);
        if (result == RESULT_OK) {
            AppointmentItem savedAppointment = (AppointmentItem) data.getSerializableExtra("APPOINTMENT_SAVE_DATA");
            if (code == 301) {
                System.out.println("MIRA: " + savedAppointment.getDate());
                insertScheduleItem(savedAppointment);
            } else if (code == 300) {
                System.out.println("Edited data: " + savedAppointment.getId());
                editScheduleItem(savedAppointment);
            }
        }
    }
}