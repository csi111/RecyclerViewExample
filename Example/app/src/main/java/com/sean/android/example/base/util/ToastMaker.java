package com.sean.android.example.base.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sean on 2017. 3. 11..
 */

public class ToastMaker {

    private ToastMaker() {

    }

    public static void makeLongToast(Context context, int id) {
        makeLongToast(context, context.getResources().getString(id));
    }

    public static void makeShortToast(Context context, int id) {
        makeShortToast(context, context.getResources().getString(id));
    }

    public static void makeLongToast(Context context, String text) {
        if (context != null && text != null && !text.isEmpty()) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }

    public static void makeShortToast(Context context, String text) {
        if (context != null && text != null && !text.isEmpty()) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

}
