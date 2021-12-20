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

import com.example.kanete.Models.Payment;
import com.example.kanete.R;

import java.util.List;

public class PaymentRecyclerViewAdapter extends RecyclerView.Adapter<PaymentRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Payment> payments;
    private LayoutInflater inflater;
    private PaymentClickListener paymentClickListener;

    public PaymentRecyclerViewAdapter(Context context, List<Payment> payments) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.payments = payments;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_payment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Payment payment = payments.get(position);
        if (payment.isDefault_pay())
            holder.imageViewCardPaymentRow.setColorFilter(ContextCompat.getColor(context, R.color.primaryColor), android.graphics.PorterDuff.Mode.SRC_IN);
        else
            holder.imageViewCardPaymentRow.setColorFilter(Color.parseColor("#FF575151"), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.textViewNameHolderPaymentRow.setText(payment.getName_holder());
        holder.textViewCreditCardPaymentRow.setText(payment.getCredit_card());
        holder.textViewExpirationDatePaymentRow.setText(String.valueOf(payment.getExpiration_date()));
        holder.textViewCVVPaymentRow.setText(payment.getCvv());
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewCardPaymentRow;
        TextView textViewNameHolderPaymentRow;
        TextView textViewCreditCardPaymentRow;
        TextView textViewExpirationDatePaymentRow;
        TextView textViewCVVPaymentRow;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewCardPaymentRow = itemView.findViewById(R.id.imageViewCardPaymentRow);
            textViewNameHolderPaymentRow = itemView.findViewById(R.id.textViewNameHolderPaymentRow);
            textViewCreditCardPaymentRow = itemView.findViewById(R.id.textViewCreditCardPaymentRow);
            textViewExpirationDatePaymentRow = itemView.findViewById(R.id.textViewExpirationDatePaymentRow);
            textViewCVVPaymentRow = itemView.findViewById(R.id.textViewCVVPaymentRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (paymentClickListener != null) paymentClickListener.onPaymentClick(view, getAdapterPosition());
        }
    }

    public Payment getPayment(int id) {
        return payments.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(PaymentClickListener itemClickListener) {
        this.paymentClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface PaymentClickListener {
        void onPaymentClick(View view, int position);
    }
}

