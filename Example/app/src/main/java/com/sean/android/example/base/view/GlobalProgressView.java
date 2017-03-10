package com.sean.android.example.base.view;

import android.content.Context;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sean on 2017. 3. 11..
 */

public class GlobalProgressView {
    private static ProgressViewDialog progressViewDialog;
    private static AtomicInteger progressReferenceCount = new AtomicInteger(0);

    public static synchronized void show(Context context) {
        if (progressReferenceCount.getAndIncrement() == 0) {
            progressViewDialog = new ProgressViewDialog(context);
            progressViewDialog.show();
        }
    }

    public static synchronized void hide() {
        if (progressReferenceCount.get() == 1) {
            progressViewDialog.dismiss();
        }

        if (progressReferenceCount.get() > 0) {
            progressReferenceCount.decrementAndGet();
        }
    }

    public static synchronized void forceHide() {
        if (progressViewDialog != null) {
            progressViewDialog.dismiss();
        }
        progressReferenceCount.set(0);
    }

}
