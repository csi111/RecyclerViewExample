package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;

/**
 * Created by Seonil on 2017-03-13.
 */

public interface ImageLoadingListener {

    void onLoadingStarted(String imageUri);

    void onLoadingComplete(String imageUri, Bitmap loadedImage);

    void onLoadingFailed(String imageUri, Throwable cause);
}
