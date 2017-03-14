package com.sean.android.example.ui;

import android.app.Application;

import com.sean.android.example.base.imageloader.ImageLoader;

/**
 * Created by sean on 2017. 3. 9..
 */

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.getInstance().init(getApplicationContext());
    }
}
