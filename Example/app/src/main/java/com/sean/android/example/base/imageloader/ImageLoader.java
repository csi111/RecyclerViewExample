package com.sean.android.example.base.imageloader;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.sean.android.example.base.imageloader.cache.LruMemoryCache;
import com.sean.android.example.base.imageloader.cache.MemoryCache;
import com.sean.android.example.base.imageloader.cache.disk.DiskCache;
import com.sean.android.example.base.imageloader.cache.disk.LruDiskCache;

import java.io.File;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageLoader {

    private ImageLoadingListener defaultImageListener;

    private MemoryCache memoryCache;
    private DiskCache diskCache;

    private ImageLoadExecutor imageLoadExecutor;


    private ImageLoader() {
        defaultImageListener = new DefaultImageLoadingListener();
    }

    public static ImageLoader getInstance() {
        return ImageLoaderHolder.instance;
    }

    private static class ImageLoaderHolder {
        public static final ImageLoader instance = new ImageLoader();
    }

    public synchronized void init(Context context) {
        if (memoryCache == null) {
            memoryCache = new LruMemoryCache();
        }

        if (diskCache == null) {
            File diskCacheDirectory = StorageUtil.getDiskCacheDirectory(context);
            diskCache = new LruDiskCache(diskCacheDirectory);
        }

        imageLoadExecutor = new ImageLoadExecutor(memoryCache, diskCache);
    }


    public void loadImage(String uri, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        loadImage(uri, new ImageViewWrapper(imageView), imageLoadingListener);

    }

    public void loadImage(String uri, ImageViewWrapper imageViewWrapper, ImageLoadingListener imageLoadingListener) {
        if (imageLoadingListener == null) {
            imageLoadingListener = defaultImageListener;
        }

        if (imageViewWrapper == null) {
            throw new IllegalArgumentException("ImageView reference must not be null");
        }


        if (TextUtils.isEmpty(uri)) {
            //TODO Cancel Image Load
        }
    }

}
