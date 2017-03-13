package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Seonil on 2017-03-13.
 */

public interface ImageLoadingListener {

    void onLoadingStarted(String imageUri);

    void onLoadingComplete(String imageUri, ImageView imageView, Bitmap loadedImage);

    void onLoadingFailed(String imageUri, ImageView imageView, Throwable cause);

    void onLoadingCancelled(String imageUri, ImageView imageView);
}
