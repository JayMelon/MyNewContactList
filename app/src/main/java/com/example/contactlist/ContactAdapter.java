package com.example.contactlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList<Contact> contactData;
    private static View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public ContactAdapter(ArrayList<Contact> arrayList, Context context){
        contactData = arrayList;
        parentContext = context;
    }

public class ContactViewHolder extends RecyclerView.ViewHolder {

    public TextView textViewContact;
    public TextView textPhone;
    public TextView textAddress;
    public TextView textCityStateZipCode;
    public Button deleteButton;
    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewContact = itemView.findViewById(R.id.textContactName);
        textPhone = itemView.findViewById(R.id.textPhoneNumber);
        textAddress = itemView.findViewById(R.id.streetAddressTextCell);
        textCityStateZipCode = itemView.findViewById(R.id.cityStateZipcodeTextCell);
        deleteButton = itemView.findViewById(R.id.buttonDeleteContact);
        //Identifies which item was clicked
        itemView.setTag(this);
        itemView.setOnClickListener(mOnItemClickListener);
    }
    //Getters for TextView so Adapter inits for each cell
    public TextView getContactTextView(){
        return textViewContact;
    }
    public TextView getPhoneTextView(){
        return textPhone;
    }
    public TextView getTextAddress() {
        return textAddress;
    }
    public TextView getTextCityStateZipCode() {
        return textCityStateZipCode;
    }

    public Button getDeleteButton(){
        return deleteButton;
    }
}

public static void setOnItemClickListener(View.OnClickListener itemClickListener){
    mOnItemClickListener = itemClickListener;
}
//Sets up adapter
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ContactViewHolder(v);
    }

//Adapter Cells
@SuppressLint("RecyclerView")
    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,  final int position) {
        ContactViewHolder cvh = (ContactViewHolder) holder;

    //Gets Address
    String currentAddress = contactData.get(position).getAddress();
    //Gets City State Zipcode in one string
    String currentCityStateZipCode = contactData.get(position).getCity() +
            ", " + contactData.get(position).getState() +
            ", " + contactData.get(position).getZipcode();

        //Alternates Color
    if(ifPositionEven(position)){
        cvh.getContactTextView().setText(contactData.get(position).getContactName());
        cvh.getContactTextView().setTextColor(Color.RED);
    }else{
        cvh.getContactTextView().setText(contactData.get(position).getContactName());
        cvh.getContactTextView().setTextColor(Color.BLUE);
    }
        cvh.getPhoneTextView().setText(contactData.get(position).getPhoneNumber());
        cvh.getTextAddress().setText(currentAddress);
        cvh.getTextCityStateZipCode().setText(currentCityStateZipCode);

        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("This button doesn't crash "+contactData.get(position).getContactID());
                    deleteItem(position);
                }
            });
        }
    else {
        cvh.getDeleteButton().setVisibility(View.INVISIBLE);
    }
    }
        public void setDelete(boolean b) {
        isDeleting = b;
        }
        private void deleteItem(int position){
            Contact contact = contactData.get(position);
            ContactDataSource ds = new ContactDataSource(parentContext);
        try{
            System.out.println("Deleting stuff");
            ds.open();
            boolean didDelete = ds.deleteContact(contact.getContactID());
            ds.close();
            if(didDelete){
                contactData.remove(position);
                notifyDataSetChanged();
            }
            else {
                Toast.makeText(parentContext,"Delete Failed",Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        }
        private boolean ifPositionEven(int position){
        return position%2 == 0;
        }

    @Override
    public int getItemCount() {
        return contactData.size();
    }

}
