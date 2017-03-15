package com.sean.android.example.base.imageloader;

import com.sean.android.example.base.imageloader.view.ImageViewWrapper;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sean on 2017. 3. 13..
 */

public class ImageInfo {

    final String uri;
    final String memoryCacheKey;
    final ImageViewWrapper imageViewWrapper;
    final ImageLoadingListener listener;
    final ImageSize imageSize;
    final ReentrantLock loadFromUriLock;

    public ImageInfo(String uri, String memoryCacheKey, ImageViewWrapper imageViewWrapper, ImageLoadingListener listener, ImageSize imageSize, ReentrantLock loadFromUriLock) {
        this.uri = uri;
        this.memoryCacheKey = memoryCacheKey;
        this.imageViewWrapper = imageViewWrapper;
        this.listener = listener;
        this.imageSize = imageSize;
        this.loadFromUriLock = loadFromUriLock;
    }
}
