package com.agonzalez.practica3_albertogonzalezhernandez;

import java.io.Serializable;

public class ContactItem implements Serializable {
    private String id;
    private String name;
    private String address;
    private String mobilePhone;
    private String homePhone;
    private String mail;
    private int labelColor;

    public ContactItem(String id, String name, String address, String mobilePhone, String homePhone, String mail, int labelColor) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.mobilePhone = mobilePhone;
        this.homePhone = homePhone;
        this.mail = mail;
        this.labelColor = labelColor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }
}
