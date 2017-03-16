package com.sean.android.example.base.imageloader;

import android.widget.ImageView;

import com.sean.android.example.base.imageloader.view.ImageViewWrapper;
import com.sean.android.example.base.imageloader.view.ViewWrapper;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sean on 2017. 3. 13..
 */

public class ImageInfo {

    private final String uri;
    private final String memoryCacheKey;
    private final ViewWrapper<ImageView> imageViewWrapper;
    private final ImageLoadingListener listener;
    private final ImageSize imageSize;

    public ImageInfo(String uri, String memoryCacheKey, ViewWrapper imageViewWrapper, ImageLoadingListener listener, ImageSize imageSize) {
        this.uri = uri;
        this.memoryCacheKey = memoryCacheKey;
        this.imageViewWrapper = imageViewWrapper;
        this.listener = listener;
        this.imageSize = imageSize;
    }


    public String getUri() {
        return uri;
    }

    public String getMemoryCacheKey() {
        return memoryCacheKey;
    }

    public ViewWrapper<ImageView> getImageViewWrapper() {
        return imageViewWrapper;
    }

    public ImageLoadingListener getLoadingListener() {
        return listener;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

}
