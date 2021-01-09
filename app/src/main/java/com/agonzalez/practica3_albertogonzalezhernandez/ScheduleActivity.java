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

        appointmentsList.add(new AppointmentItem("1", "Finish the android practice", "18/02/2021"));
        appointmentsList.add(new AppointmentItem("2", "Go to sleep early", "10/12/2021"));
        appointmentsList.add(new AppointmentItem("3", "Prepare thee breakfast", "08/07/2022"));

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
                //ContactItem selectedContact = contactsList.get(contactPos);
                //Intent contactInfoIntent = new Intent(this, ContactDetailActivity.class);
                //contactInfoIntent.putExtra("CONTACT_INFO_DATA", selectedContact);
                //startActivityForResult(contactInfoIntent, 200);
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
        //int newContactId = contactsAppDb.insertContact(contact);
        //if (newContactId > -1) {
        appointment.setId(String.valueOf(UUID.randomUUID()));
        appointmentsList.add(appointment);
        scheduleAdapter.notifyItemInserted(appointmentsList.size() - 1);
        String insertAppointmentFeedback = getResources().getString(R.string.insertAppointmentFeedback);
        Snackbar.make(scheduleRecyclerView, appointmentTitle + " " + insertAppointmentFeedback, Snackbar.LENGTH_LONG).show();
        //}
    }

    private void deleteScheduleItem(int pos){
        String appointmentTitle = appointmentsList.get(pos).getTitle();
        // contactsAppDb.deleteContact(contactsList.get(pos).getId());
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
                insertScheduleItem(savedAppointment);
            } else if (code == 200) {
                System.out.println("Edited data: " + savedAppointment.getId());
                //editContactItem(savedContact);
            }
        }
    }
}