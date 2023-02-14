package com.example.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Calendar;

public class ContactDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ContactDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertContact(Contact c) {
        boolean didSucceed = false;
        try {
            //Stores values from the current Contact.
            ContentValues initialValues = new ContentValues();
            initialValues.put(DatabaseHelper.COLUMN_CONTACT_NAME, c.getContactName());
            initialValues.put(DatabaseHelper.COLUMN_CONTACT_ADDRESS, c.getAddress());
            initialValues.put(DatabaseHelper.COLUMN_CONTACT_CITY, c.getCity());
            initialValues.put(DatabaseHelper.COLUMN_CONTACT_STATE, c.getState());
            initialValues.put(DatabaseHelper.COLUMN_CONTACT_ZIPCODE, c.getZipcode());
            initialValues.put(DatabaseHelper.COLUMN_CONTACT_PHONENUMBER, c.getPhoneNumber());
            initialValues.put(DatabaseHelper.COLUMN_CONTACT_CELLNUMBER, c.getCellNumber());
            initialValues.put(DatabaseHelper.COLUMN_CONTACT_EMAIL, c.getEMail());
            initialValues.put(DatabaseHelper.COLUMN_CONTACT_BIRTHDAY, String.valueOf(c.getBirthday().getTimeInMillis()));
            //If a row was inserted return 1
            didSucceed = database.insert(DatabaseHelper.CONTACT_TABLE, null, initialValues) > 0;


        } catch (Exception e) {

        }
        return didSucceed;
    }

    public boolean updateContact(Contact c) {
        boolean didSucceed = false;
        try {
            //Retirives contact ID
            Long rowID = (long) c.getContactID();
            ContentValues updateValues = new ContentValues();
            updateValues.put(DatabaseHelper.COLUMN_CONTACT_NAME, c.getContactName());
            updateValues.put(DatabaseHelper.COLUMN_CONTACT_ADDRESS, c.getAddress());
            updateValues.put(DatabaseHelper.COLUMN_CONTACT_CITY, c.getCity());
            updateValues.put(DatabaseHelper.COLUMN_CONTACT_STATE, c.getState());
            updateValues.put(DatabaseHelper.COLUMN_CONTACT_ZIPCODE, c.getZipcode());
            updateValues.put(DatabaseHelper.COLUMN_CONTACT_PHONENUMBER, c.getPhoneNumber());
            updateValues.put(DatabaseHelper.COLUMN_CONTACT_CELLNUMBER, c.getCellNumber());
            updateValues.put(DatabaseHelper.COLUMN_CONTACT_EMAIL, c.getEMail());
            updateValues.put(DatabaseHelper.COLUMN_CONTACT_BIRTHDAY, String.valueOf(c.getBirthday().getTimeInMillis()));
            //If table is updated if the Contact ids are Identical.
            String whereClause  = DatabaseHelper.COLUMN_CONTACT_ID+"= ";
            didSucceed = database.update(DatabaseHelper.CONTACT_TABLE, updateValues, whereClause + rowID, null) > 0;
        } catch (Exception e) {

        }
        return didSucceed;
    }
    //If the database executed correctly return 1 else do nothing
    public boolean deleteContact(int contactId){
        boolean didDelete = false;
        try{
            System.out.println("Help");
            didDelete = database.delete(DatabaseHelper.CONTACT_TABLE,DatabaseHelper.COLUMN_CONTACT_ID+" = "+contactId,null)>0;
        }catch(Exception e ){

        }
        return didDelete;
    }


    public int getLastContactID() {
        int lastId;
        try {
            String query = "Select MAX(" + DatabaseHelper.COLUMN_CONTACT_ID + ") from " + DatabaseHelper.CONTACT_TABLE;
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();

        } catch (Exception e) {
            //If no results LastID is -1
            lastId = -1;
        }
        return lastId;
    }

    //Return an array that gets everything
    public ArrayList<Contact> getContacts(){
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try{
            String query = "SELECT * FROM "+DatabaseHelper.CONTACT_TABLE;
            Cursor cursor = database.rawQuery(query, null);

            Contact newContact;

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                newContact = new Contact();
                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipcode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.seteMail(cursor.getString(8));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(calendar);
                contacts.add(newContact);
                cursor.moveToNext();
            }
            cursor.close();
        }catch(Exception e ){
            contacts = new ArrayList<Contact>();
        }
        return contacts;
    }
    //Return an array that gets everything but filtered
    public ArrayList<Contact> getContacts(String sortField, String sortOrder){
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try{
            String query = "SELECT * FROM "+DatabaseHelper.CONTACT_TABLE +" ORDER BY "+sortField+" "+sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Contact newContact;

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                newContact = new Contact();
                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipcode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.seteMail(cursor.getString(8));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(calendar);
                contacts.add(newContact);
                cursor.moveToNext();
            }
            cursor.close();
        }catch(Exception e ){
            contacts = new ArrayList<Contact>();
        }
        return contacts;
    }
    //Gets specific customer on giving ID
    public Contact getSpecficContact(int contactId) {
        Contact contact = new Contact();
        String query = "SELECT * FROM "+DatabaseHelper.CONTACT_TABLE+" WHERE " + DatabaseHelper.COLUMN_CONTACT_ID + "=" + contactId;
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            contact.setContactID(cursor.getInt(0));
            contact.setContactName(cursor.getString(1));
            contact.setAddress(cursor.getString(2));
            contact.setCity(cursor.getString(3));
            contact.setState(cursor.getString(4));
            contact.setZipcode(cursor.getString(5));
            contact.setPhoneNumber(cursor.getString(6));
            contact.setCellNumber(cursor.getString(7));
            contact.seteMail(cursor.getString(8));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
            contact.setBirthday(calendar);

            cursor.close();
        }
        return contact;
    }

}