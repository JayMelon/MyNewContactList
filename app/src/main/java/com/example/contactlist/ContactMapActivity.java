package com.example.contactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity {
    ImageButton settingsButton, mapButton, listButton;
    GoogleMap gMap;
    SupportMapFragment mapFragment;
    ArrayList<Contact> contacts;
    Contact currentContact = null;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    TextView textDirection;
    private static final int REQUEST_LOCATION_PERMISSION = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);
        initMapTypeButtons();
        getMapData();
        initCompass();

        //Ask permission for location, If user declines Snackbar will say you must have location enabled to use some features.
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(ContactMapActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ContactMapActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Snackbar.make(findViewById(R.id.activity_contact_map), "MyContactList requires this permission to locate",
                                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(
                                                ContactMapActivity.this,
                                                new String[]{
                                                        Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                                    }
                                })
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(ContactMapActivity.this,
                                new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_LOCATION_PERMISSION);
                    }
                } else {
                    startMap();
                }
            } else {
                startMap();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        createLocationRequest();
        createLocationCallBack();


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
//Method that starts the map
    private void startMap() {
        //Creates empty array list to gather contacts
        contacts = new ArrayList<>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        Bundle extras = getIntent().getExtras();
        try {
            ContactDataSource ds = new ContactDataSource(ContactMapActivity.this);
            ds.open();
            if (extras != null) {
                currentContact = ds.getSpecficContact(extras.getInt("contactid"));
            } else {
                contacts = ds.getContacts(DatabaseHelper.COLUMN_CONTACT_NAME, "ASC");
            }
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this,"Contact(s) cound not be retrived.",Toast.LENGTH_LONG).show();
        }
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                if (ContextCompat.checkSelfPermission(ContactMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    gMap = googleMap;
                    gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    startLocationUpdates();
                    Point size = new Point();
                    WindowManager w = getWindowManager();
                    w.getDefaultDisplay().getSize(size);
                    int measureWidth = size.x;
                    int measureHeight = size.y;
                    if(contacts.size()>0){
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for(int i=0; i<contacts.size(); i++){
                            currentContact = contacts.get(i);

                            Geocoder geo = new Geocoder(ContactMapActivity.this);
                            List<Address> addresses = null;
                            String address = currentContact.getAddress() +
                                    ", "+currentContact.getCity() +
                                    ", " + currentContact.getState() + " " +
                                    currentContact.getZipcode();
                            try{
                                addresses = geo.getFromLocationName(address, 1);
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                            LatLng point = new LatLng(addresses.get(0).getLatitude(),
                                    addresses.get(0).getLongitude());
                            builder.include(point);
                            gMap.addMarker(new MarkerOptions().position(point)).setTitle(currentContact.getContactName());
                        }
                        gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),measureWidth,measureHeight,450));
                    }
                    else{
                        if(currentContact != null){
                            Geocoder geo = new Geocoder(ContactMapActivity.this);
                            List<Address> addresses = null;
                            String address = currentContact.getAddress() +
                                    ", "+currentContact.getCity() +
                                    ", " + currentContact.getState() + " " +
                                    currentContact.getZipcode();
                            try{
                                addresses = geo.getFromLocationName(address, 1);
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                            LatLng point = new LatLng(addresses.get(0).getLatitude(),
                                    addresses.get(0).getLongitude());
                            gMap.addMarker(new MarkerOptions().position(point)).setTitle(currentContact.getContactName());
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
                        }
                        else{
                            AlertDialog alertDialog = new AlertDialog.Builder(ContactMapActivity.this).create();
                            alertDialog.setTitle("No Data");
                            alertDialog.setMessage("No Data is available for the mapping function.");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            alertDialog.show();
                        }
                    }
                }
            }
        });
    }
    private void initMapTypeButtons() {
        RadioGroup mapTypeGroup = findViewById(R.id.map_type);
        mapTypeGroup.check(R.id.mapTypenormal);
        mapTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mapTypenormal:
                        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.mapTypeSatellite:
                        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                }
            }
        });
    }
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Navigate Buttons
    //Method that Launches Settings Activity
    private void launchSettings(View v) {
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

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                }
            }
        };
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        gMap.setMyLocationEnabled(true);
    }

    private void getMapData() {
        Bundle extras = getIntent().getExtras();
        try {
            ContactDataSource ds = new ContactDataSource(ContactMapActivity.this);
            ds.open();
            if (extras != null) {
                currentContact = ds.getSpecficContact(extras.getInt("contactid"));
            } else {
                contacts = ds.getContacts(DatabaseHelper.COLUMN_CONTACT_NAME, "ASC");
            }
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this,"Contact(s) cound not be retrived.",Toast.LENGTH_LONG).show();
        }
    }

private void initCompass(){
        //Initilizing sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    accelerometer =  sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    if(accelerometer!=null && magnetometer != null){
        sensorManager.registerListener(mySensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(mySensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
        //If sensors are not found make toast.
    }else{
        Toast.makeText(this, "Sensors not found", Toast.LENGTH_SHORT).show();
    }
    textDirection = (TextView) findViewById(R.id.gpsText);

}
    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        float[] accelerometerValues;
        float[] magneticValues;
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                accelerometerValues = event.values;
            }
            if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                magneticValues = event.values;
            }
            //If sensors are found
            if(accelerometerValues!=null && magneticValues!=null){
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R,I,accelerometerValues,magneticValues);
                if(success){
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R,orientation);

                    float azimut = (float) Math.toDegrees(orientation[0]);
                    if (azimut < 0.0f){
                        azimut+=360.0f;
                    }
                    String direction;
                    if(azimut>=315 || azimut < 45 ){
                        direction = "N";

                    }
                    else if(azimut>= 225 && azimut < 315){
                        direction = "W";
                    }
                    else if(azimut>= 135 && azimut < 225){
                        direction = "S";
                    }
                    else{
                        direction = "E";
                    }
                    textDirection.setText(direction);
                    }
                }
            }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}