package com.example.locationfinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;

public class EditAddress extends AppCompatActivity {

    private SQLiteDBHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

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

        // Pre-fill the fields with the existing address data
        Intent intent = getIntent();
        int addressId = intent.getIntExtra("ADDRESS_ID", -1);
        String addressTitle = intent.getStringExtra("ADDRESS_TITLE");
        String addressDesc = intent.getStringExtra("ADDRESS_ADDRESS");
        String longitude = intent.getStringExtra("ADDRESS_LONGITUDE");
        String latitude = intent.getStringExtra("ADDRESS_LATITUDE");

        // Pre-fill the fields
        EditText titleInput = findViewById(R.id.textInputEditTitle);
        EditText descriptionInput = findViewById(R.id.editAddress);
        EditText longitudeInput = findViewById(R.id.editLongitude);
        EditText latitudeInput = findViewById(R.id.editLatitude);

        titleInput.setText(addressTitle);
        descriptionInput.setText(addressDesc);
        longitudeInput.setText(longitude);
        latitudeInput.setText(latitude);

        // Set up the save button to save the address
        findViewById(R.id.updateNote).setOnClickListener(view -> saveAddress(addressId, titleInput, descriptionInput, longitudeInput, latitudeInput));
    }

    private void saveAddress(int addressId, EditText titleInput, EditText descriptionInput, EditText longitudeInput, EditText latitudeInput) {
        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String longitude = longitudeInput.getText().toString();
        String latitude = latitudeInput.getText().toString();

        if (title.isEmpty() || description.isEmpty() || longitude.isEmpty() || latitude.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

        Address address = new Address(addressId, title, description, Double.parseDouble(longitude), Double.parseDouble(latitude));

        boolean isUpdated = DBHelper.updateAddress(address);
        if (isUpdated) {
            Toast.makeText(this, "Address updated!", Toast.LENGTH_LONG).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Error updating address!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteAddress(View v) {
        Intent intent = getIntent();
        int addressId = intent.getIntExtra("ADDRESS_ID", -1);

        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this address?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    DBHelper.deleteAddressById(addressId);
                    Toast.makeText(this, "Address deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
