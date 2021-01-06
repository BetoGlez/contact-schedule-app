package com.agonzalez.practica3_albertogonzalezhernandez;

public interface ItemTouchAdapter {
    void onInsertItem(ContactItem contact);
    void onEditItem(int position, ContactItem editedContact);
    void onDeleteItem(int position);
}
