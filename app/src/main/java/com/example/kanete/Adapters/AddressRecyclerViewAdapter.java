package com.example.kanete.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanete.Models.Address;
import com.example.kanete.R;

import java.util.List;

public class AddressRecyclerViewAdapter extends RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Address> addresses;
    private LayoutInflater inflater;
    private AddressClickListener addressClickListener;

    public AddressRecyclerViewAdapter(Context context, List<Address> addresses) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.addresses = addresses;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_address_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Address address = addresses.get(position);
        if (address.isDefault_adr())
            holder.imageViewPinAddressRow.setColorFilter(ContextCompat.getColor(context, R.color.primaryColor), android.graphics.PorterDuff.Mode.SRC_IN);
        else
            holder.imageViewPinAddressRow.setColorFilter(Color.parseColor("#FF575151"), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.textViewContactNameAddressRow.setText(address.getContact_name());
        holder.textViewPhoneAddressRow.setText(address.getPhone_number());
        holder.textViewZipCodeAddressRow.setText(String.valueOf(address.getZipcode()));
        holder.textViewStreetAddressRow.setText(address.getStreet());
        holder.textViewNumberAddressRow.setText(address.getNumber());
        holder.textViewCityAddressRow.setText(address.getCity());
        holder.textViewCountryAddressRow.setText(address.getCountry());
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewPinAddressRow;
        TextView textViewContactNameAddressRow;
        TextView textViewPhoneAddressRow;
        TextView textViewZipCodeAddressRow;
        TextView textViewStreetAddressRow;
        TextView textViewNumberAddressRow;
        TextView textViewCityAddressRow;
        TextView textViewCountryAddressRow;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewPinAddressRow = itemView.findViewById(R.id.imageViewPinAddressRow);
            textViewContactNameAddressRow = itemView.findViewById(R.id.textViewContactNameAddressRow);
            textViewPhoneAddressRow = itemView.findViewById(R.id.textViewPhoneAddressRow);
            textViewZipCodeAddressRow = itemView.findViewById(R.id.textViewZipCodeAddressRow);
            textViewStreetAddressRow = itemView.findViewById(R.id.textViewStreetAddressRow);
            textViewNumberAddressRow = itemView.findViewById(R.id.textViewNumberAddressRow);
            textViewCityAddressRow = itemView.findViewById(R.id.textViewCityAddressRow);
            textViewCountryAddressRow = itemView.findViewById(R.id.textViewCountryAddressRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (addressClickListener != null) addressClickListener.onAddressClick(view, getAdapterPosition());
        }
    }

    public Address getAddress(int id) {
        return addresses.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(AddressClickListener itemClickListener) {
        this.addressClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface AddressClickListener {
        void onAddressClick(View view, int position);
    }
}
