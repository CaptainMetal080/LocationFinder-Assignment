package com.example.locationfinder;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NewAddress extends AppCompatActivity {

    private SQLiteDBHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address); // Make sure your XML layout is named activity_new_address.xml

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        DBHelper = new SQLiteDBHelper(this);

        // Set up button click listener to save the address
        Button saveButton = findViewById(R.id.button);  // This is the "Save Address" button
        saveButton.setOnClickListener(this::saveAddress);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveAddress(View v) {
        EditText titleInput = findViewById(R.id.textInputNewTitle);
        EditText addressInput = findViewById(R.id.NewAddress);
        EditText longitudeInput = findViewById(R.id.NewLongitude);
        EditText latitudeInput = findViewById(R.id.NewLatitude);

        String title = titleInput.getText().toString();
        String address = addressInput.getText().toString();
        String longitudeString = longitudeInput.getText().toString();
        String latitudeString = latitudeInput.getText().toString();

        // Check if any of the fields are empty
        if (title.isEmpty() || address.isEmpty() || longitudeString.isEmpty() || latitudeString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if the longitude and latitude are valid numbers and within the range of 10 to 99
        double longitude = 0;
        double latitude = 0;
        boolean isLongitudeValid = false;
        boolean isLatitudeValid = false;

        try {
            longitude = Double.parseDouble(longitudeString);
            if (longitude >= 10 && longitude <= 99) {
                isLongitudeValid = true; // Longitude is valid
            } else {
                Toast.makeText(this, "Longitude must be between 10 and 99", Toast.LENGTH_LONG).show();
            }
        } catch (NumberFormatException e) {
            // Longitude is invalid, show a Toast
            Toast.makeText(this, "Please enter a valid longitude", Toast.LENGTH_LONG).show();
        }

        try {
            latitude = Double.parseDouble(latitudeString);
            if ( latitude <= -10 && latitude >= -99|| latitude >= 10 && latitude <= 99) {
                isLatitudeValid = true; // Latitude is valid
            } else {
                Toast.makeText(this, "Latitude must be between 10 and 99", Toast.LENGTH_LONG).show();
            }
        } catch (NumberFormatException e) {
            // Latitude is invalid, show a Toast
            Toast.makeText(this, "Please enter a valid latitude", Toast.LENGTH_LONG).show();
        }

        // Only proceed if both longitude and latitude are valid and within the range
        if (isLongitudeValid && isLatitudeValid) {
            // Create a new Address object
            Address addressObj = new Address(0, title, address, longitude, latitude);

            // Save the address into the database
            SQLiteDBHelper dbHelper = new SQLiteDBHelper(this);
            boolean isInserted = dbHelper.insertAddress(addressObj);
            if (isInserted) {
                Toast.makeText(this, "Address saved!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Error saving address!", Toast.LENGTH_LONG).show();
            }
        }
    }

}
