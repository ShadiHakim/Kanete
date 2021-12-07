package com.example.kanete.helper;

import android.app.Activity;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static void goTo(final Activity this_activity, final Class<?> activity) {
        Intent i = new Intent(this_activity, activity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this_activity.startActivity(i);
        this_activity.finish();
    }

    public static String getCurrentDate(){
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }
}
