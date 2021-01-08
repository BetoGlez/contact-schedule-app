package com.agonzalez.practica3_albertogonzalezhernandez;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.UUID;

public class ExternalStorageSD {

    public ExternalStorageSD() {
    }

    public void writeDataOnExternalStorage(List<ContactItem> contactList) {
        if (isExternalStorageReady()) {
            createFile(contactList);
        }
    }

    private boolean isExternalStorageReady() {
        String state = Environment.getExternalStorageState();
        return  state.equals(Environment.MEDIA_MOUNTED);
    }

    private void createFile(List<ContactItem> contactList) {
        try
        {
            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ContactApp");
            path.mkdirs();
            File file = new File(path, "contactList_" + UUID.randomUUID().toString() + ".json");
            OutputStreamWriter fileOut = new OutputStreamWriter(new FileOutputStream(file));
            String formattedJson = "[\n" + composeContactsJsonString(contactList) + "]";
            fileOut.write(formattedJson);
            fileOut.close();
            System.out.println("File saved to storage: " + file.getName());
        } catch (Exception ex) {
            System.out.println("There was an error while writing to SD card \n");
            System.out.println(ex);
        }
    }

    private String composeContactsJsonString(List<ContactItem> contactList) {
        String contactListJson = "";
        for(ContactItem contact: contactList) {
            String contactJson = String.format("{\n\"id\": \"%s\",\n\"name\": \"%s\",\n\"address\": \"%s\",\n\"mobilePhone\": \"%s\",\n\"homePhone\": \"%s\",\n\"mail\": \"%s\",\n\"labelColor\": \"%s\"\n},\n",
                    contact.getId(), contact.getName(), contact.getAddress(), contact.getMobilePhone(), contact.getHomePhone(), contact.getMail(), contact.getLabelColor());
            contactListJson += contactJson;
        }
        return contactListJson;
    }
}
