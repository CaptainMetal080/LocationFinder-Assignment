package com.example.locationfinder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddressAdapter.AddressClickListener {

    private SQLiteDBHelper DBHelper;
    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper = new SQLiteDBHelper(this);
        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.searchView);

        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadAddresses(newText);
                return true;
            }
        });

        loadAddresses("");  // Initially load all addresses
    }

    // Reload the list of addresses (search functionality)
    private void loadAddresses(String query) {
        List<Address> addresses = new ArrayList<>();
        Cursor cursor;
        if (query.isEmpty()) {
            cursor = DBHelper.retrieveAllAddresses();
        } else {
            cursor = DBHelper.retrieveAddressesByTitle(query);
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_TITLE));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_ADDRESS));
                @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_LONGITUDE));
                @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_LATITUDE));

                addresses.add(new Address(id, title, address, longitude, latitude));
            } while (cursor.moveToNext());
            cursor.close();
        }

        addressAdapter = new AddressAdapter(addresses, this);
        recyclerView.setAdapter(addressAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Launch the New Address Activity
    public void NewAddress(View v) {
        Intent i = new Intent(this, NewAddress.class);
        startActivity(i);
    }

    // Handle long click on an address (for deletion)
    public void AddressLongClick(Address address) {
        showDeleteConfirmationDialog(address);
    }

    // Handle single click to edit an address
    public void AddressClick(Address address) {
        Intent i = new Intent(this, EditAddress.class);
        i.putExtra("ADDRESS_ID", address.getId());
        i.putExtra("ADDRESS_TITLE", address.getTitle());
        i.putExtra("ADDRESS_ADDRESS", address.getAddress());
        i.putExtra("ADDRESS_LONGITUDE", Double.toString(address.getLongitude()));
        i.putExtra("ADDRESS_LATITUDE", Double.toString(address.getLatitude()));
        startActivity(i);
    }

    // Show delete confirmation dialog
    private void showDeleteConfirmationDialog(Address address) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this address?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    DBHelper.deleteAddressById(address.getId());
                    loadAddresses("");  // Reload addresses after deletion
                    Toast.makeText(this, "Address deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddresses("");  // Reload addresses when coming back
    }
}
