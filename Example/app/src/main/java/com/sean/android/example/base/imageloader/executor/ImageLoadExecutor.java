package com.sean.android.example.base.imageloader.executor;

import com.sean.android.example.base.imageloader.cache.ImageCacheManager;
import com.sean.android.example.base.imageloader.task.DisplayImageTask;
import com.sean.android.example.base.imageloader.task.LoadImageTask;
import com.sean.android.example.base.imageloader.view.ViewWrapper;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageLoadExecutor {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));


    public static final int THREAD_PRIORITY = Thread.NORM_PRIORITY - 1;

    private final Map<Integer, String> cacheImages;

    private Executor imageLoadExecutor; // Network통신을 통한 Image Bitmap Load & Display Threadpool Priority less than distributor
    private Executor imageLoadExecutorForCachedImage; // Cache Image Bitmap Load & Display Threadpool Priority less than distributor
    private Executor imageLoadDistributor; // CachingData 존재 여부 확인 후 각 Threadpool에게 Task 할당 Priority Normal(5)

    private AtomicBoolean isPause = new AtomicBoolean(false);
    private final Object pauseLock = new Object(); //Task에 대한 명시적 synchronized Lock Object

    private ImageCacheManager imageCacheManager;


    public ImageLoadExecutor(ImageCacheManager imageCacheManager) {
        this.imageCacheManager = imageCacheManager;
        cacheImages = Collections.synchronizedMap(new HashMap<Integer, String>());
        initializeLoadTask();
        imageLoadDistributor = createCachedExecutor();

    }

    public void submit(final LoadImageTask task) {
        imageLoadDistributor.execute(new Runnable() {
            @Override
            public void run() {
                File imageFile = imageCacheManager.getFileFromDiskCache(task.getUri());
                boolean isImageCache = (imageFile != null && imageFile.exists() && (imageFile.length() > 0));
                resetExecutorIfNeed();
                if (isImageCache) {
                    imageLoadExecutorForCachedImage.execute(task);
                } else {
                    imageLoadExecutor.execute(task);
                }
            }
        });
    }

    public void submit(final DisplayImageTask task) {
        resetExecutorIfNeed();
        imageLoadExecutorForCachedImage.execute(task);
    }


    private void initializeLoadTask() {
        if (imageLoadExecutor == null) {
            imageLoadExecutor = createExecutor();
        }

        if (imageLoadExecutorForCachedImage == null) {
            imageLoadExecutorForCachedImage = createExecutor();
        }
    }

    private void resetExecutorIfNeed() {
        if (((ExecutorService) imageLoadExecutor).isShutdown()) {
            imageLoadExecutor = createExecutor();
        }

        if (((ExecutorService) imageLoadExecutorForCachedImage).isShutdown()) {
            imageLoadExecutorForCachedImage = createExecutor();
        }
    }

    public void stop() {
        ((ExecutorService) imageLoadExecutor).shutdownNow();
        ((ExecutorService) imageLoadExecutorForCachedImage).shutdownNow();
        cacheImages.clear();
    }

    public void pause() {
        isPause.set(true);
    }

    public void resume() {
        isPause.set(false);
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
    }

    public String getLoadingImage(ViewWrapper imageViewWrapper) {
        return cacheImages.get(imageViewWrapper.getId());
    }

    public void prepareShowImageTask(ViewWrapper imageViewWrapper, String cacheKey) {
        cacheImages.put(imageViewWrapper.getId(), cacheKey);
    }

    public void cancelShowImageTask(ViewWrapper imageViewWrapper) {
        cacheImages.remove(imageViewWrapper.getId());
    }

    private Executor createExecutor() {
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>();
        return new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE, 0, TimeUnit.MILLISECONDS, blockingQueue, new ImageThreadFactory(THREAD_PRIORITY));
    }

    private Executor createCachedExecutor() {
        return Executors.newCachedThreadPool(new ImageThreadFactory(Thread.NORM_PRIORITY));
    }


    public AtomicBoolean getPause() {
        return isPause;
    }

    public Object getPauseLock() {
        return pauseLock;
    }

    public ImageCacheManager getImageCacheManager() {
        return imageCacheManager;
    }

}
