package com.agonzalez.practica3_albertogonzalezhernandez;

public class ContactItem {
    private String name;
    private String phone;
    private int labelColor;

    public ContactItem(String name, String phone, int labelColor) {
        this.name = name;
        this.phone = phone;
        this.labelColor = labelColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }
}
