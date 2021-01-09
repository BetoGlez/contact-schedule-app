package com.agonzalez.practica3_albertogonzalezhernandez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

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

    private void deleteScheduleItem(int pos){
        String appointmentTitle = appointmentsList.get(pos).getTitle();
        // contactsAppDb.deleteContact(contactsList.get(pos).getId());
        appointmentsList.remove(pos);
        scheduleAdapter.notifyItemRemoved(pos);
        String deleteFeedback = getResources().getString(R.string.deletedFeedback);
        Snackbar.make(scheduleRecyclerView, appointmentTitle + " " + deleteFeedback, Snackbar.LENGTH_LONG).show();
    }
}