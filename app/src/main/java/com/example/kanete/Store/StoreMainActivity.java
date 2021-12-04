package com.example.kanete.Store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kanete.R;
import com.example.kanete.Store.ui.account.AccountStoreFragment;
import com.example.kanete.Store.ui.addproducts.AddProductFragment;
import com.example.kanete.Store.ui.myproducts.MyProductsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class StoreMainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    final Fragment fragment1 = new MyProductsFragment();
    final Fragment fragment2 = new AddProductFragment();
    final Fragment fragment3 = new AccountStoreFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main);

        init();
    }

    public void init(){
        navigation = (BottomNavigationView) findViewById(R.id.nav_view_store);
        navigation.setOnNavigationItemSelectedListener(onSelect);

        fm.beginTransaction().add(R.id.nav_host_fragment_activity_store_main, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_store_main, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_activity_store_main,fragment1, "1").commit();

        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        navigation.setVisibility(isOpen ? View.GONE : View.VISIBLE);
                    }
                });
    }

    BottomNavigationView.OnNavigationItemSelectedListener onSelect = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_my_products:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.navigation_add_product:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.navigation_account_store:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
            }
            return false;
        }
    };


}