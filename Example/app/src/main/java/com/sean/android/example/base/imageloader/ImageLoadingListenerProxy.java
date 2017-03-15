package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageLoadingListenerProxy implements ImageLoadingListener {

    @Override
    public void onLoadingStarted(String imageUri) {

    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

    }

    @Override
    public void onLoadingFailed(String imageUri, View view, Throwable cause) {

    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
