package com.example.kanete.Customer.Payment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.Payment;

import java.util.List;

public class PaymentViewModel extends ViewModel {

    private Payment default_payment;

    public PaymentViewModel() {
        default_payment = new Payment();
    }

    public LiveData<Boolean> addPayment(Payment payment) {
        return payment.addPayment(default_payment);
    }

    public LiveData<List<Payment>> getMyPayment(){
        return default_payment.getMyPayment();
    }

    public LiveData<Boolean> changeDefault(Payment new_default_payment) {
        return new_default_payment.updateDefault(default_payment);
    }

    public List<Payment> updatedDefaults(List<Payment> payments, Payment new_default_payment){
        payments.stream()
                .filter(item -> item.getID()
                        .equals(default_payment.getID()))
                .peek(item -> item.setDefault_pay(false));
        payments.stream()
                .filter(item -> item.getID()
                        .equals(new_default_payment.getID()))
                .peek(item -> item.setDefault_pay(true));
        return payments;
    }
}
