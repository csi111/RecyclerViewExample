package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;

import com.sean.android.example.base.imageloader.cache.MemoryCache;
import com.sean.android.example.base.imageloader.cache.disk.DiskCache;
import com.sean.android.example.base.protocol.Client;
import com.sean.android.example.base.protocol.UrlConnectionClient;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Seonil on 2017-03-13.
 */

public class LoadImageTask implements Runnable {

    ImageLoadExecutor imageLoadExecutor;

    private MemoryCache memoryCache;
    private DiskCache diskCache;

    private Client imageDownloader;

    private String memoryCacheKey;

    private ImageViewWrapper imageViewWrapper;

    public LoadImageTask(MemoryCache memoryCache, DiskCache diskCache) {
        this.memoryCache = memoryCache;
        this.diskCache = diskCache;
        imageDownloader = new UrlConnectionClient();
    }

    @Override
    public void run() {
        if (isPaused()) return;


        Bitmap bitmap;

        try {
            bitmap = memoryCache.get(memoryCacheKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isPaused() {
        AtomicBoolean pause = imageLoadExecutor.getPause();

        if (pause.get()) {
            synchronized (imageLoadExecutor.getPauseLock()) {
                if (pause.get()) {
                    try {
                        imageLoadExecutor.getPauseLock().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            }
        }
        return isTaskNotActual();
    }

    private boolean isTaskNotActual() {
        return isViewGarbageCollected() || isViewReused();
    }

    private boolean isViewGarbageCollected() {
        if (imageViewWrapper.isGarbageCollected()) {
            return true;
        }
        return false;
    }

    private boolean isViewReused() {
        String currentCacheKey = imageLoadExecutor.getLoadingImage(imageViewWrapper);
        // Check whether memory cache key (image URI) for current ImageAware is actual.
        // If ImageAware is reused for another task then current task should be cancelled.
        boolean imageViewWrapperReused = !currentCacheKey.equals(currentCacheKey);
        if (imageViewWrapperReused) {
            return true;
        }
        return false;
    }
}
