package com.example.kanete.OrderManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kanete.Adapters.OrderRecyclerViewAdapter;
import com.example.kanete.Adapters.ProductRecyclerViewAdapter;
import com.example.kanete.Customer.Address.AddressActivity;
import com.example.kanete.Customer.ui.home.HomeFragment;
import com.example.kanete.Models.Order;
import com.example.kanete.Models.Product;
import com.example.kanete.R;

import java.util.List;

public class OrdersListActivity extends AppCompatActivity implements OrderRecyclerViewAdapter.OrderClickListener{

    private OrderViewModel orderViewModel;

    private OrderRecyclerViewAdapter orderRecyclerViewAdapter;
    private RecyclerView recyclerViewOrderList;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        init();
    }

    private void init() {
        orderViewModel = new OrderViewModel(this);
        recyclerViewOrderList = findViewById(R.id.recyclerViewOrderList);
        dialog = new Dialog(OrdersListActivity.this);

        setup_recyclerViewProducts();
    }

    public void setup_recyclerViewProducts(){
        orderViewModel.getOrders().observeForever(new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                recyclerViewOrderList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(getApplicationContext(), orders);
                orderRecyclerViewAdapter.setClickListener(OrdersListActivity.this);
                recyclerViewOrderList.setAdapter(orderRecyclerViewAdapter);
            }
        });
    }

    @Override
    public void onOrderClick(View view, int position) {
        Order order = orderRecyclerViewAdapter.getOrder(position);
        if (orderViewModel.getType().equals("Store")){
            storeDialog(order);
        }
        else {
            customerDialog(order);
        }
    }

    public void storeDialog(Order order){
        dialog.setContentView(R.layout.dialog_edit_order_status);

        EditText editTextOrderStatus = dialog.findViewById(R.id.editTextOrderStatus);
        Button buttonUpdateOrderStatus = dialog.findViewById(R.id.buttonUpdateOrderStatus);

        editTextOrderStatus.setText(order.getStatus());

        buttonUpdateOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_status = editTextOrderStatus.getText().toString();
                order.setStatus(new_status);

                orderViewModel.updateOrderStatus(order).observeForever(new Observer<Boolean>() {
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

    public void customerDialog(Order order){
        dialog.setContentView(R.layout.dialog_view_order_status);

        TextView textViewOrderStatus = dialog.findViewById(R.id.textViewOrderStatus);
        Button buttonOrderComplete = dialog.findViewById(R.id.buttonOrderComplete);

        textViewOrderStatus.setText(order.getStatus());

        buttonOrderComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setComplete(true);

                orderViewModel.setOrderComplete(order).observeForever(new Observer<Boolean>() {
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
}