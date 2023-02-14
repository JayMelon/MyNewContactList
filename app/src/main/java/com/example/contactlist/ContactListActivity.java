package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;


public class ContactListActivity extends AppCompatActivity {
    ImageButton settingsButton, mapButton, listButton;
    ArrayList<Contact> contacts;
    ContactAdapter contactAdapter;
    RecyclerView contactList;
    String sortField;
    String sortOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initRecycleView();
        initAddContactButton();
        initDeleteSwitch();
        ContactAdapter.setOnItemClickListener(onItemClickListener);
        //Creating objects references
        settingsButton = findViewById(R.id.settingsButton);
        mapButton = findViewById(R.id.mapButton);
        listButton = findViewById(R.id.contactlistButton);
        //Gathers preferences






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
                launchList(view);
            }
        });


    }
    //When the activity is resuming get the Preferences so it can be changed without reboot.
    @Override
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences(ContactSettingsActivity.ContactList_Preferences,
                Context.MODE_PRIVATE).getString(ContactSettingsActivity.sortFieldKey,DatabaseHelper.COLUMN_CONTACT_NAME);
        String sortOrder = getSharedPreferences(ContactSettingsActivity.ContactList_Preferences,
                Context.MODE_PRIVATE).getString(ContactSettingsActivity.sortOrderFieldKey,"ASC");
        ContactDataSource ds = new ContactDataSource(this);
        try {
            ds.open();
            contacts = ds.getContacts(sortBy, sortOrder);
            ds.close();
            if(contacts.size()>0){
                contactList = findViewById(R.id.rvContacts);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                contactList.setLayoutManager(layoutManager);
                contactAdapter = new ContactAdapter(contacts,this);
                contactList.setAdapter(contactAdapter);
            } else {
                //If the user has no contacts, Direct them to the main
                Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }catch(Exception e ){
            Toast.makeText(this,"Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
    }
    //Method that initilzing the Add contactbutton
    private void initAddContactButton(){
        Button newContact = findViewById(R.id.buttonAddContact);
        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Method that initilzing Recycleview
    private void initRecycleView(){
        ContactDataSource ds = new ContactDataSource(this);
        ArrayList<Contact> contacts;
//Gets stuff from DBS
        try {
            ds.open();
            contacts = ds.getContacts(sortField, sortOrder);
            ds.close();
            //Inits Recycler view
            RecyclerView contactlist = findViewById(R.id.rvContacts);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            contactlist.setLayoutManager(layoutManager);
            //Inits the Adapt to the Recycleview
            contactAdapter = new ContactAdapter(contacts,this);
            contactlist.setAdapter(contactAdapter);
        }catch(Exception e){
            Toast.makeText(this,"Error retrieving contacts",Toast.LENGTH_LONG).show();
        }
    }
    private void initDeleteSwitch(){
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Boolean status = compoundButton.isChecked();
                contactAdapter.setDelete(status);
                contactAdapter.notifyDataSetChanged();

            }
        });
    }


    //Implements a new OnClick to a ViewHolder Reference that will pass ID to Mainactivity
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder)
                    //What ever Holder was clicked return the tag.
                    view.getTag();
            int position = viewHolder.getAdapterPosition();
            int contactID = contacts.get(position).getContactID();
            Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
            intent.putExtra("contactid", contactID);
            startActivity(intent);
        }
    };

    //Method that Launches Settings Activity
    private void launchSettings(View v ){
        Intent i = new Intent(ContactListActivity.this, ContactSettingsActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }
    //Method that launches Home List
    private void launchList(View v) {
        Intent i = new Intent(ContactListActivity.this, MainActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    //Method that launches MapContact List
    private void launchMap(View v) {
        Intent i = new Intent(ContactListActivity.this, ContactMapActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    }