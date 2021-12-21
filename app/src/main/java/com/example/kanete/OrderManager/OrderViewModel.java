package com.example.kanete.OrderManager;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.Order;
import com.example.kanete.Models.User;

import java.util.List;

public class OrderViewModel extends ViewModel {

    private Activity this_activity;
    private String type;

    public OrderViewModel(Activity activity) {
        this.this_activity = activity;
        this.type = activity.getIntent().getStringExtra("Extra");
    }

    public String getType() {
        return type;
    }

    public LiveData<List<Order>> getOrders() {
        if (type.equals("Store"))
            return Order.getMyOrders(User.types.Store);
        else
            return Order.getMyOrders(User.types.Customer);
    }

    public LiveData<Boolean> updateOrderStatus(Order order){
        return order.updateStatus();
    }

    public LiveData<Boolean> setOrderComplete(Order order){
        return order.updateComplete();
    }
}
