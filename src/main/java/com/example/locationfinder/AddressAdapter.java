package com.example.locationfinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {

    private List<Address> addressList;
    private AddressClickListener listener;

    // Interface to handle item clicks
    public interface AddressClickListener {
        void AddressLongClick(Address address);
        void AddressClick(Address address);
    }

    public AddressAdapter(List<Address> addressList, AddressClickListener listener) {
        this.addressList = addressList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_address_view, parent, false);
        return new AddressHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressHolder holder, int position) {
        Address address = addressList.get(position);
        holder.textViewTitle.setText(address.getTitle());
        holder.textViewAddress.setText(address.getAddress());
        holder.textViewLatitude.setText("Latitude: " + address.getLatitude());
        holder.textViewLongitude.setText("Longitude: " + address.getLongitude());

        // Handle long click (delete)
        holder.itemView.setOnLongClickListener(v -> {
            listener.AddressLongClick(address);
            return true;
        });

        // Handle single click (edit)
        holder.itemView.setOnClickListener(v -> {
            listener.AddressClick(address);
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewAddress;
        public TextView textViewLatitude;
        public TextView textViewLongitude;

        public AddressHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titlemain);
            textViewAddress = itemView.findViewById(R.id.address_name);
            textViewLatitude = itemView.findViewById(R.id.latitude);
            textViewLongitude = itemView.findViewById(R.id.longitude);
        }
    }
}
