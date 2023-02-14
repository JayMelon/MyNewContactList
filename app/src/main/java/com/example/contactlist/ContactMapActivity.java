package com.example.contactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity {
    ImageButton settingsButton, mapButton, listButton;
    Button locationButton;
    LocationManager locationManager;
    LocationListener gpsListener;
    private static final int LOCATION_PERM_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);
        //Creating objects references
        initGetLocationButton();





        //Navigate Buttons
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
                launchList(view);
            }
        });
    }

        //Location Button method
        private void initGetLocationButton() {
            locationButton = findViewById(R.id.buttonGetLocation);
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        String address;
                        EditText editAddress = (EditText) findViewById(R.id.editAddress);
                        EditText editCity = (EditText) findViewById(R.id.editCity);
                        EditText editState = (EditText) findViewById(R.id.editState);
                        EditText editZipcode = (EditText) findViewById(R.id.editZipcode);

                        address = editAddress.getText().toString() + ", " +
                                editCity.getText().toString() + ", " +
                                editState.getText().toString() + ", " +
                                editZipcode.getText().toString();

                        List<Address> addresses = null;
                        Geocoder geo = new Geocoder(ContactMapActivity.this);
                        try {
                            addresses = geo.getFromLocationName(address, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        TextView txtLatitude = (TextView) findViewById(R.id.latitudeText);
                        TextView txtLongitude = (TextView) findViewById(R.id.longitudeText);
                        try {
                            txtLatitude.setText(String.valueOf(addresses.get(0).getLatitude()));
                            txtLongitude.setText(String.valueOf(addresses.get(0).getLongitude()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }
            });
        }
        //Updates the location while using GPS instead a click of a button.
        private void getLocationUsingGps(){
        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);

            gpsListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.latitudeText);
                    TextView txtLongitude = (TextView) findViewById(R.id.longitudeText);
                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            /*
            locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,0,0,gpsListener);
*/
        }catch(Exception e ){
            e.printStackTrace();
        }
        }
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERM_CODE);
    }






@Override
public void onPause(){
        super.onPause();
        try{
            locationManager.removeUpdates(gpsListener);

        }
        catch (Exception e){
            e.printStackTrace();
        }
}

private boolean isLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }












    //Navigate Buttons
    //Method that Launches Settings Activity
    private void launchSettings(View v ){
        Intent i = new Intent(ContactMapActivity.this, ContactSettingsActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }
    //Method that launches Contact List
    private void launchList(View v) {
        Intent i = new Intent(ContactMapActivity.this, ContactListActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    //Method that launches MapContact List
    private void launchMap(View v) {
        Intent i = new Intent(ContactMapActivity.this, MainActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}