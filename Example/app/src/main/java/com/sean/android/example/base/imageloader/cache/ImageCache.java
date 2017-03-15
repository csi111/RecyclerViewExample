package com.sean.android.example.base.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.sean.android.example.base.imageloader.ImageSize;
import com.sean.android.example.base.imageloader.StorageUtil;
import com.sean.android.example.base.imageloader.cache.disk.ImageDiskCache;
import com.sean.android.example.base.imageloader.cache.disk.LruDiskCache;
import com.sean.android.example.base.imageloader.cache.memory.ImageMemoryCache;
import com.sean.android.example.base.imageloader.cache.memory.LruMemoryCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seonil on 2017-03-15.
 */

public class ImageCache {
    private ImageDiskCache imageDiskCache;
    private ImageMemoryCache imageMemoryCache;


    public ImageCache() {
    }

    public synchronized void init(Context context) {
        if (imageMemoryCache == null) {
            imageMemoryCache = new LruMemoryCache();
        }

        if (imageDiskCache == null) {
            File diskCacheDirectory = StorageUtil.getDiskCacheDirectory(context);
            imageDiskCache = new LruDiskCache(diskCacheDirectory);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        Bitmap bitmap = null;

        if (imageMemoryCache != null) {
            bitmap = imageMemoryCache.get(key);
        }

        return bitmap;
    }

    public File getFileFromDiskCache(String key) {
        File file = null;

        if (imageDiskCache != null) {
            file = imageDiskCache.get(key);
        }

        return file;
    }

    public void put(String uri, Bitmap bitmap) {
        if (bitmap != null && !TextUtils.isEmpty(uri)) {
            imageMemoryCache.put(uri, bitmap);
        }
    }

    public boolean save(String uri, Bitmap bitmap) {
        if (bitmap != null && !TextUtils.isEmpty(uri)) {
            try {
                return imageDiskCache.save(uri, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean save(String uri, InputStream inputStream) throws IOException {
        boolean isSaved = false;
        if (inputStream != null && !TextUtils.isEmpty(uri)) {
            isSaved = imageDiskCache.save(uri, inputStream);
        }

        return isSaved;
    }

    public void clear() {
        if (imageDiskCache != null) {
            imageDiskCache.clear();
        }

        if (imageMemoryCache != null) {
            imageMemoryCache.clear();
        }
    }

    public void close() {
        if (imageMemoryCache != null) {
            imageMemoryCache.close();
            imageMemoryCache = null;
        }

        if (imageDiskCache != null) {
            imageDiskCache.close();
            imageDiskCache = null;
        }
    }


    private String generateKey(String imageUri, ImageSize targetSize) {
        return new StringBuilder(imageUri).append("-").append(targetSize.getWidth()).append("x").append(targetSize.getHeight()).toString();
    }

}
