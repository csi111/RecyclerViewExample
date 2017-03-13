package com.sean.android.example.base.imageloader;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;

import com.sean.android.example.base.imageloader.cache.MemoryCache;
import com.sean.android.example.base.imageloader.cache.disk.DiskCache;
import com.sean.android.example.base.protocol.Client;
import com.sean.android.example.base.protocol.RequestData;
import com.sean.android.example.base.protocol.ResponseData;
import com.sean.android.example.base.protocol.UrlConnectionClient;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Seonil on 2017-03-13.
 */

public class LoadImageTask implements Runnable {

    private ImageLoadExecutor imageLoadExecutor;

    private ImageInfo imageInfo;

    private MemoryCache memoryCache;
    private DiskCache diskCache;

    private Client imageDownloader;

    private String memoryCacheKey;

    private ImageViewWrapper imageViewWrapper;

    private final String uri;

    private final String cacheKey;

    private final ImageLoadingListener imageLoadingListener;

    private ImageLoadType imageLoadType = ImageLoadType.NETWORK;

    private final Handler handler;

    private int imageWidth;
    private int imageHeight;


    public LoadImageTask(ImageLoadExecutor imageLoadExecutor, ImageInfo imageInfo, Handler handler) {
        this.imageLoadExecutor = imageLoadExecutor;
        this.imageInfo = imageInfo;
        this.uri = imageInfo.uri;
        this.handler = handler;
        this.cacheKey = imageInfo.memoryCacheKey;
        this.imageWidth = imageInfo.imageWidth;
        this.imageHeight = imageInfo.imageHeight;
        this.imageViewWrapper = imageInfo.imageViewWrapper;
        this.imageLoadingListener = imageInfo.listener;


        imageDownloader = new UrlConnectionClient();
    }

    @Override
    public void run() {
        if (isPaused()) return;


        ReentrantLock loadFromUriLock = imageInfo.loadFromUriLock;

        loadFromUriLock.lock();
        Bitmap bitmap;

        try {
            bitmap = memoryCache.get(memoryCacheKey);

            if (bitmap == null || bitmap.isRecycled()) {
                bitmap = tryLoadBitmap();
                if (bitmap == null)
                    return;

                checkTaskNotActual();
                checkTaskInterrupted();


                if (bitmap != null) { // Memory Caching
                    memoryCache.put(memoryCacheKey, bitmap);
                }
            } else {
                imageLoadType = imageLoadType.MEMORY_CACHE;
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            InputStream imageInputStream = diskCache.get(uri);
            if (imageInputStream != null && imageInputStream.available() > 0) {
                imageLoadType = ImageLoadType.DISK_CACHE;

                checkTaskNotActual();
                bitmap = decodeImage(imageInputStream, imageWidth, imageHeight);
            }
            if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
                imageLoadType = ImageLoadType.NETWORK;
                ResponseData responseData = imageDownloader.call(new RequestData(uri, null));
                imageInputStream = responseData.getResponseBody().in();

                checkTaskNotActual();
                bitmap = decodeImage(imageInputStream, imageWidth, imageHeight);

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

    public Bitmap decodeImage(InputStream inputStream, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // BEGIN_INCLUDE (calculate_sample_size)
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
        // END_INCLUDE (calculate_sample_size)
    }

    private class TaskException extends Exception {

    }


}
