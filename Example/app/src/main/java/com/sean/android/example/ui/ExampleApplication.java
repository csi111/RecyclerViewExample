package com.sean.android.example.ui;

import android.app.Application;
import android.os.Build;

import com.sean.android.example.R;
import com.sean.android.example.base.imageloader.ImageLoader;

/**
 * Created by sean on 2017. 3. 9..
 */

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.getInstance().init(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImageLoader.getInstance().setFailedImage(getDrawable(R.drawable.default_no_image));
        } else {
            ImageLoader.getInstance().setFailedImage(getApplicationContext().getResources().getDrawable(R.drawable.default_no_image));
        }
    }
}
