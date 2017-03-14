package com.sean.android.example.base.imageloader;

import com.sean.android.example.base.imageloader.cache.MemoryCache;
import com.sean.android.example.base.imageloader.cache.disk.DiskCache;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageLoadExecutor {
    public static final int THREAD_POOL_SIZE = 4;
    public static final int THREAD_PRIORITY = Thread.NORM_PRIORITY - 1;

    private final Map<Integer, String> cacheImages;
    private final Map<String, ReentrantLock> uriLocks;

    Executor imageLoadExecutor;
    Executor imageLoadExecutorForCachedImage; // 고정된 Thread갯수를 가진 Threadpool
    Executor imageLoadDistributor; // 동적 Threadpool

    private AtomicBoolean isPause = new AtomicBoolean(false);
    private final Object pauseLock = new Object();

    MemoryCache memoryCache;
    DiskCache diskCache;


    public ImageLoadExecutor(MemoryCache memoryCache, DiskCache diskCache) {
        this.memoryCache = memoryCache;
        this.diskCache = diskCache;
        cacheImages = Collections.synchronizedMap(new HashMap<Integer, String>());
        uriLocks = new WeakHashMap<String, ReentrantLock>();
        initializeLoadTask();
        imageLoadDistributor = createCachedExecutor();

    }

    void submit(final LoadImageTask task) {
        imageLoadDistributor.execute(new Runnable() {
            @Override
            public void run() {
                File imageFile = diskCache.get(task.getUri());
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

    void submit(final DisplayImageTask task) {
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

    void stop() {
        ((ExecutorService) imageLoadExecutor).shutdownNow();
        ((ExecutorService) imageLoadExecutorForCachedImage).shutdownNow();

        cacheImages.clear();
    }

    void pause() {
        isPause.set(true);
    }

    void resume() {
        isPause.set(false);
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
    }

    String getLoadingImage(ImageViewWrapper imageViewWrapper) {
        return cacheImages.get(imageViewWrapper.getId());
    }

    void prepareShowImageTask(ImageViewWrapper imageViewWrapper, String cacheKey) {
        cacheImages.put(imageViewWrapper.getId(), cacheKey);
    }

    void cancelShowImageTask(ImageViewWrapper imageViewWrapper) {
        cacheImages.remove(imageViewWrapper.getId());
    }

    private Executor createExecutor() {
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>();
        return new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE, 0, TimeUnit.MILLISECONDS, blockingQueue, new ImageThreadFactory(THREAD_PRIORITY, "image_pool_executor"));
    }

    private Executor createCachedExecutor() {
        return Executors.newCachedThreadPool(new ImageThreadFactory(Thread.NORM_PRIORITY, "image_pool_dynamic_executor"));
    }


    public AtomicBoolean getPause() {
        return isPause;
    }

    public Object getPauseLock() {
        return pauseLock;
    }


    ReentrantLock getLockForUri(String uri) {
        ReentrantLock lock = uriLocks.get(uri);

        if (lock == null) {
            lock = new ReentrantLock();
            uriLocks.put(uri, lock);
        }
        return lock;
    }
}
