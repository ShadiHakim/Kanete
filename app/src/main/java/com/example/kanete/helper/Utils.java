package com.example.kanete.helper;

import android.app.Activity;
import android.content.Intent;

public class Utils {

    public static void goTo(final Activity this_activity, final Class<?> activity) {
        Intent i = new Intent(this_activity, activity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this_activity.startActivity(i);
        this_activity.finish();
    }
}
