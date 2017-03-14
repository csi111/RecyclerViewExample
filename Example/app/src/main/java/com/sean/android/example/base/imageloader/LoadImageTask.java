package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;
import android.os.Handler;

import com.sean.android.example.base.imageloader.cache.MemoryCache;
import com.sean.android.example.base.imageloader.cache.disk.DiskCache;
import com.sean.android.example.base.imageloader.cache.disk.LruDiskCache;
import com.sean.android.example.base.protocol.Client;
import com.sean.android.example.base.protocol.ConnectException;
import com.sean.android.example.base.protocol.RequestData;
import com.sean.android.example.base.protocol.ResponseData;
import com.sean.android.example.base.protocol.UrlConnectionClient;
import com.sean.android.example.base.util.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Seonil on 2017-03-13.
 */

public class LoadImageTask implements Runnable, LruDiskCache.DiskCopyListener {


    private ImageLoadExecutor imageLoadExecutor;

    private ImageInfo imageInfo;

    private MemoryCache memoryCache;
    private DiskCache diskCache;

    private Client imageDownloader;

    private ImageDecoder imageDecoder;

    private ImageViewWrapper imageViewWrapper;

    private final String uri;

    private final String cacheKey;

    private final ImageLoadingListener imageLoadingListener;

    private ImageLoadType imageLoadType = ImageLoadType.NETWORK;

    private final Handler handler;

    private final ImageSize imageSize;

    public LoadImageTask(ImageLoadExecutor imageLoadExecutor, ImageInfo imageInfo, Handler handler) {
        this.imageLoadExecutor = imageLoadExecutor;
        this.memoryCache = imageLoadExecutor.memoryCache;
        this.diskCache = imageLoadExecutor.diskCache;
        this.imageInfo = imageInfo;
        this.uri = imageInfo.uri;
        this.handler = handler;
        this.cacheKey = imageInfo.memoryCacheKey;
        this.imageSize = imageInfo.imageSize;
        this.imageViewWrapper = imageInfo.imageViewWrapper;
        this.imageLoadingListener = imageInfo.listener;

        imageDecoder = new DefaultImageDecoder();
        imageDownloader = new UrlConnectionClient();
    }

    @Override
    public void run() {
        if (isPaused()) return;


        ReentrantLock loadFromUriLock = imageInfo.loadFromUriLock;

        loadFromUriLock.lock();
        Bitmap bitmap;

        try {
            bitmap = memoryCache.get(cacheKey);



            if (bitmap == null || bitmap.isRecycled()) {
                bitmap = tryLoadBitmap();
                if (bitmap == null)
                    return;

                checkTaskNotActual();
                checkTaskInterrupted();


                if (bitmap != null) { // Memory Caching
                    Logger.d(this, "CacheKey =[" + cacheKey + "], Put Bitmap into MemoryCache");
                    memoryCache.put(cacheKey, bitmap);
                }
            } else {
                Logger.d(this, "CacheKey =[" + cacheKey + "], Get Bitmap from MemoryCache");
                imageLoadType = imageLoadType.MEMORY_CACHE;
            }

        } catch (Exception e) {
            fireCancelEvent();
            return;
        } finally {
            loadFromUriLock.unlock();
        }

        DisplayImageTask displayBitmapTask = new DisplayImageTask(bitmap, imageInfo, imageLoadExecutor);
        runTask(displayBitmapTask, false, handler);
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

    private Bitmap tryLoadBitmap() throws TaskException {
        Bitmap bitmap = null;
        try {
            File imageFile = diskCache.get(uri);
            if (imageFile != null && imageFile.exists() && imageFile.length() > 0) {
                imageLoadType = ImageLoadType.DISK_CACHE;

                checkTaskNotActual();
                bitmap = decodeImage(StorageUtil.getFilePath(imageFile));
            }
            if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
                imageLoadType = ImageLoadType.NETWORK;

                //TODO Save DiskCache
                tryCacheImageOnDisk();
                imageFile = diskCache.get(uri);
                checkTaskNotActual();
                bitmap = decodeImage(StorageUtil.getFilePath(imageFile));

                if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
                    fireFailEvent(null);
                }
            }
        } catch (IllegalStateException e) {
            fireFailEvent(e);
        } catch (TaskException e) {
            throw e;
        } catch (IOException e) {
            fireFailEvent(e);
        } catch (OutOfMemoryError e) {
            fireFailEvent(e);
        } catch (Throwable e) {
            fireFailEvent(e);
        }
        return bitmap;
    }


    private void checkTaskNotActual() throws TaskException {
        checkViewGarbageCollected();
        checkViewReused();
    }

    private void checkTaskInterrupted() throws TaskException {
        if (isTaskInterrupted()) {
            throw new TaskException();
        }
    }

    private void checkViewReused() throws TaskException {
        if (isViewReused()) {
            throw new TaskException();
        }
    }

    private void checkViewGarbageCollected() throws TaskException {
        if (isViewGarbageCollected()) {
            throw new TaskException();
        }
    }

    private boolean isTaskInterrupted() {
        if (Thread.interrupted()) {
            return true;
        }
        return false;
    }

    private boolean isTaskNotActual() {
        return isViewGarbageCollected() || isViewReused();
    }

    public String getUri() {
        return uri;
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

        if (currentCacheKey == null) {
            Logger.d(this, "CurrentCacheKey is null");
        }

        boolean imageViewWrapperReused = !cacheKey.equals(currentCacheKey);
        if (imageViewWrapperReused) {
            return true;
        }
        return false;
    }

    private void fireFailEvent(final Throwable failCause) {
        if (isTaskInterrupted() || isTaskNotActual()) return;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                imageLoadingListener.onLoadingFailed(uri, imageViewWrapper.getWrappedView(), failCause);
            }
        };
        runTask(r, false, handler);
    }

    private void fireCancelEvent() {
        if (isTaskInterrupted()) return;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                imageLoadingListener.onLoadingCancelled(uri, imageViewWrapper.getWrappedView());
            }
        };
        runTask(r, false, handler);
    }


    static void runTask(Runnable r, boolean sync, Handler handler) {
        if (sync) {
            r.run();
        } else {
            handler.post(r);
        }
    }

    private boolean tryCacheImageOnDisk() throws TaskException {

        boolean loaded;
        try {
            loaded = downloadImage();
        } catch (IOException e) {
            loaded = false;
            fireFailEvent(e.getCause());
        } catch (ConnectException e) {
            fireFailEvent(e.getCause());
            loaded = false;
        }
        return loaded;
    }

    private boolean downloadImage() throws ConnectException, IOException {
        ResponseData responseData = imageDownloader.call(new RequestData(uri, null));
        InputStream is = responseData.getResponseBody().in();

        if (is == null) {
            return false;
        } else {
            try {
                return diskCache.save(uri, is, this);
            } finally {
                is.close();
            }
        }
    }


    private Bitmap decodeImage(String imageUri) throws IOException {
        ImageDecodingInfo imageDecodingInfo = new ImageDecodingInfo(cacheKey, imageUri, uri, imageSize);
        return imageDecoder.decode(imageDecodingInfo);
    }


    @Override
    public boolean bytesCopied(int current, int total) {
        return true;
    }

    private class TaskException extends Exception {

    }


}
