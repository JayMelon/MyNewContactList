package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {
    //Objects needed that will be used Globally
    ContactDataSource ds = new ContactDataSource(this);
    ImageButton settingsButton, mapButton, listButton;
    private Contact currentContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToggleButton();
        initChangeDateButton();
        initTextChangedEvents();
        initSavebutton();
        setTitle("Home");
        //If there is something in the intent populate the Contact details in the Editor, Else leave blank
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            initContact(extras.getInt("contactid"));
        }
        else {
            currentContact = new Contact();
        }
        setForEditing(false);



        //Creating objects references
        settingsButton = findViewById(R.id.settingsButton);
        mapButton = findViewById(R.id.mapButton);
        listButton = findViewById(R.id.contactlistButton);







        //Settings button
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchSettings(view);
            }
        });
        //Map Button
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchMap(view);
            }
        });
        //Contact List button;
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Contact> contacts = new ArrayList<Contact>();
                try {
                    ds.open();
                    contacts = ds.getContacts();
                    ds.close();
                    if (contacts.size() > 0) {
                        launchList(view);
                    } else {
                        //Do nothing if text is higher
                    }
                }catch(Exception e ){

                }
            }
        });


    }

private void initTextChangedEvents(){
        //Edit Contact name;
        final EditText etContact = findViewById(R.id.editName);
        etContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
currentContact.setContactName(etContact.getText().toString());

            }
        });
        //Edit Address
        final EditText etAddress = findViewById(R.id.editAddress);
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentContact.setAddress(etAddress.getText().toString());
            }
        });
        //Edit City
    final EditText etCity = findViewById(R.id.editCity);
    etCity.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            currentContact.setCity(etCity.getText().toString());
        }
    });
    //Edit State
    final EditText etState = findViewById(R.id.editState);
    etState.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            currentContact.setState(etState.getText().toString());
        }
    });
    //Edit ZipCode
    final EditText etZipcode = findViewById(R.id.editZipcode);
    etZipcode.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            currentContact.setZipcode(etZipcode.getText().toString());
        }
    });
    //Edit PhoneNumber
    final EditText etPhoneNumber = findViewById(R.id.editHome);
    etPhoneNumber.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            currentContact.setPhoneNumber(etPhoneNumber.getText().toString());
        }
    });
    //Edit CellNumber
    final EditText etCellNumber = findViewById(R.id.editCell);
    etCellNumber.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            currentContact.setCellNumber(etCellNumber.getText().toString());
        }
    });
    //Edit Email
    final EditText etEmail = findViewById(R.id.editEMail);
    etEmail.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            currentContact.seteMail(etEmail.getText().toString());
        }

    });
    //Setting Phone text listeners to format
etPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
etCellNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
}

    //Initialize Toggle button
    private void initToggleButton(){
        ToggleButton toggleButton = findViewById(R.id.toggleButtonEdit);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setForEditing method, passing it true if the button is toggled for editing and false if it is not.
                setForEditing(toggleButton.isChecked());
            }
        });
    }
    //Initialize Change date button
    private void initChangeDateButton(){
        Button changeDate = findViewById(R.id.buttonBirthday);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm,"DatePick");
            }
        });
    }
    //Initialize Save Button updates to DBS
    private void initSavebutton(){
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                ContactDataSource ds = new ContactDataSource(MainActivity.this);
                try {
                    //Opens database to writable so we can add/update stuff.
                    ds.open();

                    if(currentContact.getContactID() == -1 ){
                        System.out.println(currentContact.getContactID());
                        //Inserts into db
                        wasSuccessful = ds.insertContact(currentContact);
                        if(wasSuccessful){
                            //If inserted, get previous id.
                            int newId = ds.getLastContactID();
                            currentContact.setContactID(newId);
                        }
                    }
                    else {
                        System.out.println("Updating");
                        wasSuccessful = ds.updateContact(currentContact);
                        System.out.println(currentContact.getAddress());
                    }
                    ds.close();


                }catch(Exception e){
                    wasSuccessful = false;
                }
                if(wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }

        });
    }
    //Initializes cont
    private void initContact(int id){
        ContactDataSource ds = new ContactDataSource(MainActivity.this);
        try {
            ds.open();
            currentContact = ds.getSpecficContact(id);
            ds.close();
        }
        catch(Exception e) {
            Toast.makeText(this,"Load contact failed", Toast.LENGTH_LONG).show();
        }
        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        EditText editPhone = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);
        TextView birthDay = findViewById(R.id.textBirthday);

        editName.setText(currentContact.getContactName());
        editAddress.setText(currentContact.getAddress());
        editCity.setText(currentContact.getCity());
        editState.setText(currentContact.getState());
        editZipCode.setText(currentContact.getZipcode());
        editPhone.setText(currentContact.getPhoneNumber());
        editCell.setText(currentContact.getCellNumber());
        editEmail.setText(currentContact.getEMail());
        birthDay.setText(DateFormat.format("MM/dd/yyyy",currentContact.getBirthday().getTimeInMillis()).toString());
    }
    //Method that enables texts
    private void setForEditing(boolean enabled){
        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        EditText editPhone = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);
        Button buttonChange = findViewById(R.id.buttonBirthday);
        Button buttonSave = findViewById(R.id.buttonSave);

        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipCode.setEnabled(enabled);
        editPhone.setEnabled(enabled);
        editCell.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        buttonChange.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
        if (enabled){
            editName.requestFocus();
        }else{
            ScrollView s = findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);
        }

}
//Hides keyboard
private void hideKeyBoard(){
    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    EditText editName = findViewById(R.id.editName);
    imm.hideSoftInputFromWindow(editName.getWindowToken(),0);
    EditText editAddress = findViewById(R.id.editAddress);
    imm.hideSoftInputFromWindow(editAddress.getWindowToken(),0);
    EditText editCity = findViewById(R.id.editCity);
    imm.hideSoftInputFromWindow(editAddress.getWindowToken(),0);
    EditText editState = findViewById(R.id.editState);
    imm.hideSoftInputFromWindow(editAddress.getWindowToken(),0);
    EditText editZipcode = findViewById(R.id.editZipcode);
    imm.hideSoftInputFromWindow(editAddress.getWindowToken(),0);
    EditText editHome = findViewById(R.id.editHome);
    imm.hideSoftInputFromWindow(editAddress.getWindowToken(),0);
    EditText editCell = findViewById(R.id.editCell);
    imm.hideSoftInputFromWindow(editAddress.getWindowToken(),0);
    EditText editEmail = findViewById(R.id.editEMail);
    imm.hideSoftInputFromWindow(editAddress.getWindowToken(),0);
}

    //Method that Launches Settings Activity
    private void launchSettings(View v ){
        Intent i = new Intent(this, ContactSettingsActivity.class);
            i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

    }
    //Method that launches Contact List
    private void launchList(View v) {
        Intent i = new Intent(MainActivity.this, ContactListActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    //Method that launches MapContact List
    private void launchMap(View v) {
        Intent i = new Intent(MainActivity.this, ContactMapActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    //Gets selected date from Datepicker dialog
    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        TextView birthDay = findViewById(R.id.textBirthday);
        birthDay.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
        currentContact.setBirthday(selectedTime);
    }
}