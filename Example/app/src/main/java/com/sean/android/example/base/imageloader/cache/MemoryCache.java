package com.sean.android.example.base.imageloader.cache;

import android.graphics.Bitmap;

import java.util.Collection;

/**
 * Created by Seonil on 2017-03-13.
 */

public interface MemoryCache {

    void put(String key, Bitmap value);

    Bitmap get(String key);

    Bitmap remove(String key);

    void clear();
}
