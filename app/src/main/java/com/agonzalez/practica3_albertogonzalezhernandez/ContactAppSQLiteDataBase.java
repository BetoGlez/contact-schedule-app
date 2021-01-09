package com.agonzalez.practica3_albertogonzalezhernandez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactAppSQLiteDataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactsApp.db";
    private static final String CONTACTS_TABLE_NAME = "contacts";

    private static final String CREATE_DATABASE = "CREATE TABLE " + CONTACTS_TABLE_NAME +
            " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + " name VARCHAR(1000) , address VARCHAR(1000), mobilePhone VARCHAR(1000), homePhone VARCHAR(1000), mail VARCHAR(1000), labelColor INTEGER)";

    public ContactAppSQLiteDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        db.execSQL(CREATE_DATABASE);
        onCreate(db);
    }

    public int insertContact(ContactItem contact) {
        SQLiteDatabase db = getWritableDatabase();
        int contactId = -1;
        if (db != null) {
            ContentValues contactValues = new ContentValues();
            int contactLabelColor = (int) ((Math.random() * (6 - 1)) + 1);
            contactValues.put("name", contact.getName());
            contactValues.put("address", contact.getAddress());
            contactValues.put("mobilePhone", contact.getMobilePhone());
            contactValues.put("homePhone", contact.getHomePhone());
            contactValues.put("mail", contact.getMail());
            contactValues.put("labelColor", contactLabelColor);

            contactId = (int) db.insert(CONTACTS_TABLE_NAME, null, contactValues);
        }
        db.close();
        return contactId;
    }

    public boolean deleteContact(String contactId)
    {
        SQLiteDatabase db = getWritableDatabase();
        boolean result = db.delete(CONTACTS_TABLE_NAME,  "id=" + contactId, null) > 0;
        db.close();
        return result;
    }

    public void editContact(ContactItem contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contactValues = new ContentValues();
        contactValues.put("name", contact.getName());
        contactValues.put("address", contact.getAddress());
        contactValues.put("mobilePhone", contact.getMobilePhone());
        contactValues.put("homePhone", contact.getHomePhone());
        contactValues.put("mail", contact.getMail());
        db.update(CONTACTS_TABLE_NAME, contactValues, "id=" + contact.getId(), null);
        db.close();
    }

    public List<ContactItem> getContacts() {
        List<ContactItem> contactsList  = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String[] contactValuesToGet = {"id", "name", "address", "mobilePhone", "homePhone", "mail", "labelColor"};
        Cursor c = db.query(CONTACTS_TABLE_NAME, contactValuesToGet, null, null, null, null, null, null);
        if (c.moveToFirst()){
            do{
                String id = String.valueOf(c.getInt(c.getColumnIndex("id")));
                String name = c.getString(c.getColumnIndex("name"));
                String address = c.getString(c.getColumnIndex("address"));
                String mobilePhone = c.getString(c.getColumnIndex("mobilePhone"));
                String homePhone = c.getString(c.getColumnIndex("homePhone"));
                String mail = c.getString(c.getColumnIndex("mail"));
                int labelColor = c.getInt(c.getColumnIndex("labelColor"));
                contactsList.add(new ContactItem(id, name, address, mobilePhone, homePhone, mail, labelColor));
            }while(c.moveToNext());
        }
        c.close();
        db.close();
        return contactsList;
    }
}
