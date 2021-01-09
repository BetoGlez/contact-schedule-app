package com.agonzalez.practica3_albertogonzalezhernandez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContactAppSQLiteDataBase contactsAppDb;
    private List<ContactItem> contactsList = new ArrayList<>();
    private RecyclerView contactsRecyclerView;
    private ContactsAdapter contactsAdapter;
    private int contactPos = 0;
    private int MY_PERMISSIONS_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE;
    private int MY_PERMISSIONS_REQUEST_ACCESS_CALL_PHONE;

    // Fab
    private FloatingActionButton addContactFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB
        contactsAppDb = new ContactAppSQLiteDataBase(getApplicationContext());
        contactsList = contactsAppDb.getContacts();

        //Permissions
        requestPermissions();

        // FAB
        addContactFab = findViewById(R.id.newAppointmentFab);
        addContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateContactDetail();
            }
        });

        // Recycler View
        loadContactsAdapter();
        setContactsAdapter();
        registerForContextMenu(contactsRecyclerView);
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_ACCESS_CALL_PHONE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle(R.string.contactListMenuHeader);
        inflater.inflate(R.menu.menu_list_actions_recview, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactsAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteOption:
                deleteContactItem(contactPos);
                return true;
            case R.id.detailsOption:
                ContactItem selectedContact = contactsList.get(contactPos);
                Intent contactInfoIntent = new Intent(this, ContactDetailActivity.class);
                contactInfoIntent.putExtra("CONTACT_INFO_DATA", selectedContact);
                startActivityForResult(contactInfoIntent, 200);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadContactsAdapter() {
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

    private void insertContactItem(ContactItem contact) {
        String contactName = contact.getName();
        int newContactId = contactsAppDb.insertContact(contact);
        if (newContactId > -1) {
            contact.setId(String.valueOf(newContactId));
            contactsList.add(contact);
            contactsAdapter.notifyItemInserted(contactsList.size() - 1);
            String insertContactFeedback = getResources().getString(R.string.insertContactFeedback);
            Snackbar.make(contactsRecyclerView, contactName + " " + insertContactFeedback, Snackbar.LENGTH_LONG).show();
        }
    }

    private void deleteContactItem(int pos){
        String contactName = contactsList.get(pos).getName();
        contactsAppDb.deleteContact(contactsList.get(pos).getId());
        contactsList.remove(pos);
        contactsAdapter.notifyItemRemoved(pos);
        String deleteFeedback = getResources().getString(R.string.deletedFeedback);
        Snackbar.make(contactsRecyclerView, contactName + " " + deleteFeedback, Snackbar.LENGTH_LONG).show();
    }

    private void editContactItem(ContactItem contactItem) {
        int pos = -1;
        ContactItem editedContact = new ContactItem("", "", "", "", "", "", -1);
        for (ContactItem contactToSearch: contactsList) {
            if (contactToSearch.getId().equals(contactItem.getId())) {
                editedContact = contactToSearch;
            }
        }
        if (!editedContact.getId().trim().isEmpty()) {
            pos = contactsList.indexOf(editedContact);
            if (pos > -1) {
                contactsAppDb.editContact(contactItem);
                contactsList.set(pos, contactItem);
                contactsAdapter.notifyItemChanged(pos, contactItem);
                String editContactFeedback = getResources().getString(R.string.editContactFeedback);
                Snackbar.make(contactsRecyclerView, editContactFeedback, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // Page navigation
    private void navigateContactDetail() {
        Intent contactDetailIntent = new Intent(this, ContactDetailActivity.class);
        startActivityForResult(contactDetailIntent, 201);
    }

    protected void onActivityResult(int code, int result, Intent data) {
        super.onActivityResult(code, result, data);
        System.out.println("Intent response code: " + code);
        if (result == RESULT_OK) {
            ContactItem savedContact = (ContactItem) data.getSerializableExtra("CONTACT_SAVE_DATA");
            if (code == 201) {
                insertContactItem(savedContact);
            } else if (code == 200) {
                System.out.println("Edited data: " + savedContact.getId());
                editContactItem(savedContact);
            }
        }
    }
}