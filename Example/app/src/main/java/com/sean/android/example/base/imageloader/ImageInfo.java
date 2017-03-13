package com.sean.android.example.base.imageloader;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sean on 2017. 3. 13..
 */

public class ImageInfo {

    final String uri;
    final String memoryCacheKey;
    final ImageViewWrapper imageViewWrapper;
    final ImageLoadingListener listener;
    final int imageWidth;
    final int imageHeight;
    final ReentrantLock loadFromUriLock;

    public ImageInfo(String uri, String memoryCacheKey, ImageViewWrapper imageViewWrapper, ImageLoadingListener listener, int imageWidth, int imageHeight, ReentrantLock loadFromUriLock) {
        this.uri = uri;
        this.memoryCacheKey = memoryCacheKey;
        this.imageViewWrapper = imageViewWrapper;
        this.listener = listener;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.loadFromUriLock = loadFromUriLock;
    }
}
