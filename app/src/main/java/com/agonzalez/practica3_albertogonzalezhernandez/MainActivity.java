package com.agonzalez.practica3_albertogonzalezhernandez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ContactItem> contactsList = new ArrayList<>();
    private RecyclerView contactsRecyclerView;
    private ContactsAdapter contactsAdapter;
    private int contactPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadContactsAdapter();
        setContactsAdapter();
        registerForContextMenu(contactsRecyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle(R.string.contactListMenuHeader);
        inflater.inflate(R.menu.menu_contact_recview, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteOption:
                deleteContactItem(contactPos);
                return true;
            case R.id.detailsOption:
                ContactItem selectedContact = contactsList.get(contactPos);
                System.out.println(selectedContact.getName());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadContactsAdapter() {
        contactsList.add(new ContactItem("Alberto González Hernández", "634270451", 1));
        contactsList.add(new ContactItem("Rafael Nadal", "633524395", 2));
        contactsList.add(new ContactItem("Lionel Messi", "9336482643", 3));
        contactsList.add(new ContactItem("Cristiano Ronaldo", "634527595", 4));
        contactsList.add(new ContactItem("Jordi Alba", "933465721", 5));
        contactsList.add(new ContactItem("Antoine Griezman", "634526399", 1));
        contactsList.add(new ContactItem("Novak Djockovic", "634521211", 2));
        contactsList.add(new ContactItem("Andres Iniesta", "635221381", 3));
        contactsList.add(new ContactItem("Javier Hernández", "7772202085", 4));
        contactsList.add(new ContactItem("Rafael Márquez", "7771104110", 5));

        contactsRecyclerView = findViewById(R.id.contactsRecycler);
        contactsAdapter = new ContactsAdapter(contactsList, this);
    }

    private void setContactsAdapter() {
        contactsAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactPos = contactsRecyclerView.getChildAdapterPosition(v);
                openContextMenu(contactsRecyclerView);
            }
        });

        contactsRecyclerView.setAdapter(contactsAdapter);
        contactsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void deleteContactItem(int pos){
        String contactName = contactsList.get(pos).getName();
        contactsList.remove(pos);
        contactsAdapter.notifyItemRemoved(pos);
        String deleteFeedback = getResources().getString(R.string.deletedFeedback);
        Snackbar.make(contactsRecyclerView, contactName + " " + deleteFeedback, Snackbar.LENGTH_LONG).show();
    }
}