package com.agonzalez.practica3_albertogonzalezhernandez;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class ContactCreateEditFragment extends Fragment {

    Context context;
    Activity activity;
    Button cancelContactBtn, saveContactBtn, mobileCallBtn, homeCallBtn;
    TextInputLayout contactNameField, contactMobileField, contactHomePhoneField, contactAddressField, contactMailField;
    LinearLayout callPhoneButtons;

    private ContactItem editableContactInfo;

    public ContactCreateEditFragment() { }

    public static ContactCreateEditFragment newInstance() {
        ContactCreateEditFragment fragment = new ContactCreateEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_create_edit, container, false);
        context = requireContext();
        activity = requireActivity();

        initListenersAndWatchers(view);

        editableContactInfo = getContactInfoData();
        if (!editableContactInfo.getName().trim().isEmpty()) {
            System.out.println("Edition mode for contact: " + editableContactInfo.getName());
            setContactData(editableContactInfo);
            mobileCallBtn.setText(getResources().getString(R.string.callMobile) + " \n" + editableContactInfo.getMobilePhone());
            homeCallBtn.setText(getResources().getString(R.string.callHomePhone) + " \n" + editableContactInfo.getHomePhone());
        } else {
            System.out.println("Creation contact mode");
            ((ViewGroup) callPhoneButtons.getParent()).removeView(callPhoneButtons);
        }

        return view;
    }

    private ContactItem getContactInfoData() {
        ContactItem contactInfo = new ContactItem("", "", "", "", "", "", -1);
        Intent i = activity.getIntent();
        if(i.getSerializableExtra("CONTACT_INFO_DATA") != null) {
            contactInfo = (ContactItem) i.getSerializableExtra("CONTACT_INFO_DATA");
        }
        return contactInfo;
    }

    private void initListenersAndWatchers(View view) {
        contactNameField = view.findViewById(R.id.contactNameField);
        contactMailField = view.findViewById(R.id.contactMailField);
        contactAddressField = view.findViewById(R.id.contactAddressField);
        contactMobileField = view.findViewById(R.id.contactMobileField);
        contactHomePhoneField = view.findViewById(R.id.contactHomePhoneField);

        cancelContactBtn = view.findViewById(R.id.cancelContactBtn);
        saveContactBtn = view.findViewById(R.id.saveContactBtn);

        mobileCallBtn = view.findViewById(R.id.callMobileBtn);
        homeCallBtn = view.findViewById(R.id.callHomePhoneBtn);
        callPhoneButtons = view.findViewById(R.id.phoneCallButtons);

        contactNameField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNotEmpty(String.valueOf(s), contactNameField);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        contactAddressField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNotEmpty(String.valueOf(s), contactAddressField);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        contactMobileField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidPhone(String.valueOf(s), contactMobileField);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        contactHomePhoneField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidPhone(String.valueOf(s), contactHomePhoneField);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        contactMailField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidEmail(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancelContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCancelBtn();
            }
        });
        saveContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSaveBtn();
            }
        });

        mobileCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactMobilePhone = editableContactInfo.getMobilePhone();
                if (!contactMobilePhone.trim().isEmpty()) {
                    startPhoneCall(contactMobilePhone);
                }
            }
        });

        homeCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactHomePhone = editableContactInfo.getHomePhone();
                if (!contactHomePhone.trim().isEmpty()) {
                    startPhoneCall(contactHomePhone);
                }
            }
        });
    }

    private void clickCancelBtn() {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    private void clickSaveBtn() {
        ContactItem savedContact = composeContact();
        if (!savedContact.getId().trim().isEmpty()) {
            Intent i = new Intent();
            i.putExtra("CONTACT_SAVE_DATA", savedContact);
            activity.setResult(Activity.RESULT_OK, i);
            activity.finish();
        } else {
            Toast toast = Toast.makeText(context, getResources().getText(R.string.completeFields), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean isValidEmail(String mail) {
        boolean isValidMail;
        if(Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            isValidMail = true;
            contactMailField.setError(null);
        } else {
            isValidMail = false;
            contactMailField.setError(getResources().getString(R.string.invalidMailError));
        }
        return isValidMail;
    }

    private boolean isValidPhone(String mail, TextInputLayout field) {
        boolean isValidPhone;
        if(Patterns.PHONE.matcher(mail).matches()) {
            isValidPhone = true;
            field.setError(null);
        } else {
            isValidPhone = false;
            field.setError(getResources().getString(R.string.invalidPhoneError));
        }
        return isValidPhone;
    }

    private boolean isNotEmpty(String text, TextInputLayout field) {
        boolean isNotEmpty;
        if(!text.trim().isEmpty()) {
            isNotEmpty = true;
            field.setError(null);
        } else {
            isNotEmpty = false;
            field.setError(getResources().getString(R.string.emptyFieldError));
        }
        return isNotEmpty;
    }

    private ContactItem composeContact() {
        ContactItem contactToSave = new ContactItem("", "", "", "", "", "",  -1);
        String contactName = contactNameField.getEditText().getText().toString().trim();
        String contactAddress = contactAddressField.getEditText().getText().toString().trim();
        String contactMobile = contactMobileField.getEditText().getText().toString().trim();
        String contactHomePhone = contactHomePhoneField.getEditText().getText().toString().trim();
        String contactMail = contactMailField.getEditText().getText().toString().trim();
        boolean areFieldsComplete = !contactName.isEmpty() && !contactAddress.isEmpty() && !contactMobile.isEmpty() && !contactHomePhone.isEmpty() && !contactMail.isEmpty();
        if (areFieldsComplete) {
            boolean isErrorInForm = !TextUtils.isEmpty(contactNameField.getError()) || !TextUtils.isEmpty(contactAddressField.getError()) || !TextUtils.isEmpty(contactMobileField.getError()) ||
                !TextUtils.isEmpty(contactHomePhoneField.getError()) || !TextUtils.isEmpty(contactMailField.getError());
            if (!isErrorInForm) {
                int contactColor = editableContactInfo.getId().trim().isEmpty() ? (int) ((Math.random() * (6 - 1)) + 1): editableContactInfo.getLabelColor();
                String contactId = editableContactInfo.getId().trim().isEmpty() ? "-1" : editableContactInfo.getId();
                contactToSave = new ContactItem(contactId, contactName, contactAddress, contactMobile, contactHomePhone, contactMail, contactColor);
            }
        }
        return contactToSave;
    }

    private void setContactData(ContactItem contactData) {
        contactNameField.getEditText().setText(contactData.getName());
        contactAddressField.getEditText().setText(contactData.getAddress());
        contactMobileField.getEditText().setText(contactData.getMobilePhone());
        contactHomePhoneField.getEditText().setText(contactData.getHomePhone());
        contactMailField.getEditText().setText(contactData.getMail());
    }

    private void startPhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel: " + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}