package com.example.kanete.Customer.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.kanete.Adapters.PaymentRecyclerViewAdapter;
import com.example.kanete.Models.Payment;
import com.example.kanete.R;

import java.util.List;

public class PaymentActivity extends AppCompatActivity implements PaymentRecyclerViewAdapter.PaymentClickListener{

    private PaymentViewModel paymentViewModel;
    private PaymentRecyclerViewAdapter paymentRecyclerViewAdapter;

    private CardView cardViewAddPayment;
    private RecyclerView recyclerViewPayment;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        init();
    }

    private void init() {
        paymentViewModel = new PaymentViewModel();
        cardViewAddPayment = findViewById(R.id.cardViewAddPayment);
        recyclerViewPayment = findViewById(R.id.recyclerViewPayment);
        dialog = new Dialog(PaymentActivity.this);

        setup_recyclerViewPayment();
        cardViewAddPayment.setOnClickListener(addPaymentlistener);
    }

    public void setup_recyclerViewPayment(){
        paymentViewModel.getMyPayment().observeForever(new Observer<List<Payment>>() {
            @Override
            public void onChanged(List<Payment> payments) {
                if (recyclerViewPayment.getAdapter() == null) {
                    recyclerViewPayment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    paymentRecyclerViewAdapter = new PaymentRecyclerViewAdapter(getApplicationContext(), payments);
                    paymentRecyclerViewAdapter.setClickListener(PaymentActivity.this);
                    recyclerViewPayment.setAdapter(paymentRecyclerViewAdapter);
                }
                else {
                    paymentRecyclerViewAdapter.setPayments(payments);
                    paymentRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public void onPaymentClick(View view, int position) {
        Payment address = paymentRecyclerViewAdapter.getPayment(position);
        paymentViewModel.changeDefault(address).observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean flag) {
                List<Payment> updated_payment = paymentViewModel.updatedDefaults(paymentRecyclerViewAdapter.getPayments(), address);
                paymentRecyclerViewAdapter.setPayments(updated_payment);
                paymentRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    View.OnClickListener addPaymentlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.setContentView(R.layout.dialog_add_payment);

            EditText editTextTextPersonNameHolderPayment = dialog.findViewById(R.id.editTextTextPersonNameHolderPayment);
            EditText editTextCreditCardPayment = dialog.findViewById(R.id.editTextCreditCardPayment);
            EditText editTextExpirationDatePayment = dialog.findViewById(R.id.editTextExpirationDatePayment);
            EditText editTextCVVPayment = dialog.findViewById(R.id.editTextCVVPayment);
            CheckBox checkBoxIsDefaultPayment = dialog.findViewById(R.id.checkBoxIsDefaultPayment);
            Button buttonAddPayment = dialog.findViewById(R.id.buttonAddPayment);

            buttonAddPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Payment payment = new Payment();
                    payment.setName_holder(editTextTextPersonNameHolderPayment.getText().toString());
                    payment.setCredit_card(editTextCreditCardPayment.getText().toString());
                    payment.setExpiration_date(editTextExpirationDatePayment.getText().toString());
                    payment.setCvv(editTextCVVPayment.getText().toString());
                    payment.setDefault_pay(checkBoxIsDefaultPayment.isChecked());

                    // TODO add checks

                    paymentViewModel.addPayment(payment).observeForever(new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean flag) {
                            if (flag)
                                dialog.dismiss();
                        }
                    });
                }
            });

            dialog.show();
        }
    };
}