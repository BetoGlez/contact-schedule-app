package com.agonzalez.practica3_albertogonzalezhernandez;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<ContactItem> contactsList = new ArrayList<>();
    private RecyclerView contactsRecyclerView;
    private ContactsAdapter contactsAdapter;
    private int contactPos = 0;

    // Fab
    private FloatingActionButton addContactFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FAB
        addContactFab = findViewById(R.id.newContactFab);
        addContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateContactDetail();
            }
        });

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
                Intent contactInfoIntent = new Intent(this, ContactDetailActivity.class);
                contactInfoIntent.putExtra("CONTACT_INFO_DATA", selectedContact);
                startActivityForResult(contactInfoIntent, 200);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadContactsAdapter() {
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
        contactsList.add(0, contact);
        contactsAdapter.notifyItemInserted(0);
        String insertContactFeedback = getResources().getString(R.string.insertContactFeedback);
        Snackbar.make(contactsRecyclerView, contactName + " " + insertContactFeedback, Snackbar.LENGTH_LONG).show();
    }

    private void deleteContactItem(int pos){
        String contactName = contactsList.get(pos).getName();
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

    private String getUniqueId() {
        return UUID.randomUUID().toString();
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