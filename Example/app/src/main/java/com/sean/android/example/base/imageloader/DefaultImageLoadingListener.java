package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Seonil on 2017-03-13.
 */

public class DefaultImageLoadingListener implements ImageLoadingListener {

    @Override
    public void onLoadingStarted(String imageUri) {

    }

    @Override
    public void onLoadingComplete(String imageUri, ImageView imageView, Bitmap loadedImage) {

    }

    @Override
    public void onLoadingFailed(String imageUri, ImageView imageView, Throwable cause) {

    }

    @Override
    public void onLoadingCancelled(String imageUri, ImageView imageView) {

    }
}
