package com.sean.android.example.base.imageloader;

import android.content.Context;

import com.sean.android.example.base.imageloader.cache.LruMemoryCache;
import com.sean.android.example.base.imageloader.cache.MemoryCache;
import com.sean.android.example.base.imageloader.cache.disk.DiskCache;
import com.sean.android.example.base.imageloader.cache.disk.LruDiskCache;

import java.io.File;
import java.io.IOException;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageLoaderConfiguration {

    private MemoryCache memoryCache;
    private DiskCache diskCache;

    public ImageLoaderConfiguration() {
    }

    public void init(Context context) {
        if(memoryCache == null) {
            memoryCache = new LruMemoryCache();
        }

        if(diskCache == null) {
            File diskCacheDirectory = StorageUtil.getDiskCacheDirectory(context);
            diskCache = new LruDiskCache(diskCacheDirectory);
        }
    }
}
