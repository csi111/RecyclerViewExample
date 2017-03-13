package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;

/**
 * Created by sean on 2017. 3. 14..
 */

public class DisplayImageTask implements Runnable {

    private final Bitmap bitmap;
    private final ImageViewWrapper imageViewWrapper;
    private final String uri;
    private final String cacheKey;
    private final ImageLoadingListener listener;
    private final ImageLoadExecutor imageLoadExecutor;

    public DisplayImageTask(Bitmap bitmap, ImageInfo imageInfo, ImageLoadExecutor imageLoadExecutor) {
        this.bitmap = bitmap;
        this.imageViewWrapper = imageInfo.imageViewWrapper;
        this.uri = imageInfo.uri;
        this.cacheKey = imageInfo.memoryCacheKey;
        this.listener = imageInfo.listener;
        this.imageLoadExecutor = imageLoadExecutor;
    }

    @Override
    public void run() {
        if (imageViewWrapper.isGarbageCollected()) {
            listener.onLoadingCancelled(uri, imageViewWrapper.getWrappedView());
        } else if (isViewWasReused()) {
            listener.onLoadingCancelled(uri, imageViewWrapper.getWrappedView());
        } else {
            imageViewWrapper.getWrappedView().setImageBitmap(bitmap);
            imageLoadExecutor.cancelShowImageTask(imageViewWrapper);
            listener.onLoadingComplete(uri, imageViewWrapper.getWrappedView(), bitmap);
        }

    }

    private boolean isViewWasReused() {
        String currentCacheKey = imageLoadExecutor.getLoadingImage(imageViewWrapper);
        return !cacheKey.equals(currentCacheKey);
    }
}
