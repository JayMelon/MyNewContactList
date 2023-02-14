package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class ContactSettingsActivity extends AppCompatActivity {
    //Setting up Constants for Preferences
    public static final String ContactList_Preferences = "ContactList_Preferences";
    public static final String sortFieldKey = "sortField";
    public static final String sortOrderFieldKey = "sortOrderField";
    //Setting up Objects in Activity
    RadioButton rbName, rbCity, rbBirthday, rbAscending, rbDescending;
    RadioGroup sortByFieldGroup, sortOrderByFieldGroup;
    //Preferences

    private String sortByPreferences, sortOrderByPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Init Objects

        initSettings();

    }

    private void initSettings() {
        //Initializing RadioGroups/Buttons
        sortByFieldGroup = findViewById(R.id.radioGroupSortBy);
        sortOrderByFieldGroup = findViewById(R.id.radioGroupSortOrder);
        rbAscending = findViewById(R.id.radioAscending);
        rbDescending = findViewById(R.id.radioDescending);
        rbName = findViewById(R.id.radioName);
        rbCity = findViewById(R.id.radioCity);
        rbBirthday = findViewById(R.id.radioBirthday);
        //On Initilatizion get preferences settings, If User has nothing saved default is contactName and ASC
        sortByPreferences = getSharedPreferences(ContactList_Preferences, Context.MODE_PRIVATE).getString(sortFieldKey,DatabaseHelper.COLUMN_CONTACT_NAME);
        sortOrderByPreferences = getSharedPreferences(ContactList_Preferences,Context.MODE_PRIVATE).getString(sortOrderFieldKey,"ASC");
        //Buttons update based on Initilzation;
        if (sortByPreferences.equalsIgnoreCase(DatabaseHelper.COLUMN_CONTACT_NAME)) {
            rbName.setChecked(true);
        } else if (sortByPreferences.equalsIgnoreCase(DatabaseHelper.COLUMN_CONTACT_CITY)) {
            rbCity.setChecked(true);
        } else {
            rbBirthday.setChecked(true);
        }
        if (sortOrderByPreferences.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        } else {
            rbDescending.setChecked(true);
        }
        //Initilizing Listeners
        initOrderByClick();
        initSortByClick();

    }

    //If SortBy click has been changed, Put the selected button values to Preferences.
    private void initSortByClick(){
        sortByFieldGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rbName.isChecked()){
                    //Set to Column Name
                    getSharedPreferences(ContactList_Preferences,Context.MODE_PRIVATE).edit().putString(sortFieldKey,DatabaseHelper.COLUMN_CONTACT_NAME).apply();
                }
                    //Set to City Column Name
                else if(rbCity.isChecked()){
                    getSharedPreferences(ContactList_Preferences,Context.MODE_PRIVATE).edit().putString(sortFieldKey,DatabaseHelper.COLUMN_CONTACT_CITY).apply();
                }
                    //Set to Birthday Column name
                else {
                    getSharedPreferences(ContactList_Preferences, Context.MODE_PRIVATE).edit().putString(sortFieldKey,DatabaseHelper.COLUMN_CONTACT_BIRTHDAY).apply();
                }
            }
        });
    }
    //If  OrderBy click has been changed, Put the selected button values to Preferences.
    private void initOrderByClick(){
        sortOrderByFieldGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rbAscending.isChecked()){
                    getSharedPreferences(ContactList_Preferences,Context.MODE_PRIVATE).edit().putString(sortOrderFieldKey,"ASC").apply();
                }
                else {
                    getSharedPreferences(ContactList_Preferences, Context.MODE_PRIVATE).edit().putString(sortOrderFieldKey,"DESC").apply();
                }
            }
        });
    }


}