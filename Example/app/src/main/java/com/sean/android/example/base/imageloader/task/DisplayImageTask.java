package com.sean.android.example.base.imageloader.task;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sean.android.example.base.imageloader.ImageInfo;
import com.sean.android.example.base.imageloader.ImageLoadingListener;
import com.sean.android.example.base.imageloader.executor.ImageLoadExecutor;
import com.sean.android.example.base.imageloader.view.ViewWrapper;

/**
 * Created by sean on 2017. 3. 14..
 */

public class DisplayImageTask implements Runnable {

    private final Bitmap bitmap;
    private final ViewWrapper<ImageView> imageViewWrapper;
    private final String uri;
    private final String memoryCacheKey;
    private final ImageLoadingListener listener;
    private final ImageLoadExecutor imageLoadExecutor;

    public DisplayImageTask(Bitmap bitmap, ImageInfo imageInfo, ImageLoadExecutor imageLoadExecutor) {
        this.bitmap = bitmap;
        this.imageViewWrapper = imageInfo.getImageViewWrapper();
        this.uri = imageInfo.getUri();
        this.memoryCacheKey = imageInfo.getMemoryCacheKey();
        this.listener = imageInfo.getLoadingListener();
        this.imageLoadExecutor = imageLoadExecutor;
    }

    @Override
    public void run() {
        if (imageViewWrapper.isGarbageCollected()) {
            listener.onLoadingCancelled(uri, imageViewWrapper.getWrappedView());
        } else if (isViewWasReused()) {
            listener.onLoadingCancelled(uri, imageViewWrapper.getWrappedView());
        } else {
            imageViewWrapper.getWrappedView().post(new Runnable() {
                @Override
                public void run() {
                    imageViewWrapper.getWrappedView().setImageBitmap(bitmap);
                    imageLoadExecutor.cancelShowImageTask(imageViewWrapper);
                    listener.onLoadingComplete(uri, imageViewWrapper.getWrappedView(), bitmap);
                }
            });

        }

    }

    private boolean isViewWasReused() {
        String currentCacheKey = imageLoadExecutor.getLoadingImage(imageViewWrapper);
        return !memoryCacheKey.equals(currentCacheKey);
    }
}
