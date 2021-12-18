package com.example.kanete.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import com.example.kanete.Models.Product;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static void goTo_emptyStack(final Activity this_activity, final Class<?> activity) {
        Intent i = new Intent(this_activity, activity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this_activity.startActivity(i);
        this_activity.finish();
    }

    public static void goTo_withProduct(final Activity this_activity, final Class<?> activity, Product selectedProduct) {
        Intent i = new Intent(this_activity, activity);
        i.putExtra("selectedProduct", selectedProduct);
        i.putExtra("userType", this_activity.getLocalClassName());
        this_activity.startActivity(i);
    }

    public static void goTo(final Activity this_activity, final Class<?> activity){
        Intent i = new Intent(this_activity, activity);
        this_activity.startActivity(i);
    }

    public static String getCurrentDate(){
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }
}
