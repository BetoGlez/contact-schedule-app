package com.agonzalez.practica3_albertogonzalezhernandez;

public interface ScheduleTouchAdapter {
    void onInsertItem(AppointmentItem appointment);
    void onEditItem(int position, AppointmentItem editedAppointment);
    void onDeleteItem(int position);
}
