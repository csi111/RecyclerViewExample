package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Seonil on 2017-03-13.
 */

public interface ImageLoadingListener {

    void onLoadingStarted(String imageUri);

    void onLoadingComplete(String imageUri, View view, Bitmap loadedImage);

    void onLoadingFailed(String imageUri, View view, Throwable cause);

    void onLoadingCancelled(String imageUri, View view);
}
