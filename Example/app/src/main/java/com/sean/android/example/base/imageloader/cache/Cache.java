package com.sean.android.example.base.imageloader.cache;

import android.graphics.Bitmap;

/**
 * Created by Seonil on 2017-03-15.
 */

public interface Cache<DATA> {

    DATA get(String imageUri);

    void put(String imageUri, Bitmap bitmap);

    void remove(String imageUri);

    void close();

    void clear();
}
