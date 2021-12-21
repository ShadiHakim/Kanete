package com.example.kanete.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanete.Models.Address;
import com.example.kanete.Models.Order;
import com.example.kanete.Models.Payment;
import com.example.kanete.Models.Product;
import com.example.kanete.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Order> orders;
    private LayoutInflater inflater;
    private OrderClickListener orderClickListener;

    public OrderRecyclerViewAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_order_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orders.get(position);

        order.getLive_product().observeForever(new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                Picasso.get().load(product.getImages().get(0)).into(holder.imageViewOrderitem);
                holder.textViewNameOrderitem.setText(product.getName());
                holder.textViewPriceOrderitem.setText(product.getPrice() + " ₪");
                holder.textViewQuantityOrderitem.setText("* " + order.getQuantity());
            }
        });

        order.getLive_address().observeForever(new Observer<Address>() {
            @Override
            public void onChanged(Address address) {
                holder.textViewContactNameAddressOrderitem.setText(address.getContact_name());
                holder.textViewPhoneAddressOrderitem.setText(address.getPhone_number());
                holder.textViewZipCodeAddressOrderitem.setText(String.valueOf(address.getZipcode()));
                holder.textViewStreetAddressOrderitem.setText(address.getStreet());
                holder.textViewNumberAddressOrderitem.setText(address.getNumber());
                holder.textViewCityAddressOrderitem.setText(address.getCity());
                holder.textViewCountryAddressOrderitem.setText(address.getCountry());
            }
        });

        order.getLive_payment().observeForever(new Observer<Payment>() {
            @Override
            public void onChanged(Payment payment) {
                holder.textViewNameHolderPaymentOrderitem.setText(payment.getName_holder());
                holder.textViewCreditCardPaymentOrderitem.setText(payment.getCredit_card());
                holder.textViewExpirationDatePaymentOrderitem.setText(String.valueOf(payment.getExpiration_date()));
                holder.textViewCVVPaymentOrderitem.setText(payment.getCvv());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewOrderitem;
        TextView textViewNameOrderitem;
        TextView textViewPriceOrderitem;
        TextView textViewQuantityOrderitem;
        Button buttonOrderitem;

        ConstraintLayout constraintLayoutAddress;
        TextView textViewContactNameAddressOrderitem;
        TextView textViewPhoneAddressOrderitem;
        TextView textViewZipCodeAddressOrderitem;
        TextView textViewStreetAddressOrderitem;
        TextView textViewNumberAddressOrderitem;
        TextView textViewCityAddressOrderitem;
        TextView textViewCountryAddressOrderitem;

        ConstraintLayout constraintLayoutPayment;
        TextView textViewNameHolderPaymentOrderitem;
        TextView textViewCreditCardPaymentOrderitem;
        TextView textViewExpirationDatePaymentOrderitem;
        TextView textViewCVVPaymentOrderitem;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewOrderitem = itemView.findViewById(R.id.imageViewOrderitem);
            textViewNameOrderitem = itemView.findViewById(R.id.textViewNameOrderitem);
            textViewPriceOrderitem = itemView.findViewById(R.id.textViewPriceOrderitem);
            textViewQuantityOrderitem = itemView.findViewById(R.id.textViewQuantityOrderitem);
            buttonOrderitem = itemView.findViewById(R.id.buttonOrderitem);

            constraintLayoutAddress = itemView.findViewById(R.id.constraintLayoutAddress);
            textViewContactNameAddressOrderitem = itemView.findViewById(R.id.textViewContactNameAddressOrderitem);
            textViewPhoneAddressOrderitem = itemView.findViewById(R.id.textViewPhoneAddressOrderitem);
            textViewZipCodeAddressOrderitem = itemView.findViewById(R.id.textViewZipCodeAddressOrderitem);
            textViewStreetAddressOrderitem = itemView.findViewById(R.id.textViewStreetAddressOrderitem);
            textViewNumberAddressOrderitem = itemView.findViewById(R.id.textViewNumberAddressOrderitem);
            textViewCityAddressOrderitem = itemView.findViewById(R.id.textViewCityAddressOrderitem);
            textViewCountryAddressOrderitem = itemView.findViewById(R.id.textViewCountryAddressOrderitem);

            constraintLayoutPayment = itemView.findViewById(R.id.constraintLayoutPayment);
            textViewNameHolderPaymentOrderitem = itemView.findViewById(R.id.textViewNameHolderPaymentOrderitem);
            textViewCreditCardPaymentOrderitem = itemView.findViewById(R.id.textViewCreditCardPaymentOrderitem);
            textViewExpirationDatePaymentOrderitem = itemView.findViewById(R.id.textViewExpirationDatePaymentOrderitem);
            textViewCVVPaymentOrderitem = itemView.findViewById(R.id.textViewCVVPaymentOrderitem);

            itemView.setOnClickListener(this);
            buttonOrderitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((Button)v).getText().equals("Expand")){
                        constraintLayoutAddress.setVisibility(View.VISIBLE);
                        constraintLayoutPayment.setVisibility(View.VISIBLE);
                        ((Button)v).setText("Shrink");
                    }
                    else {
                        constraintLayoutAddress.setVisibility(View.GONE);
                        constraintLayoutPayment.setVisibility(View.GONE);
                        ((Button)v).setText("Expand");
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (orderClickListener != null) orderClickListener.onOrderClick(view, getAdapterPosition());
        }
    }

    public Order getOrder(int id) {
        return orders.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(OrderClickListener itemClickListener) {
        this.orderClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface OrderClickListener {
        void onOrderClick(View view, int position);
    }
}
