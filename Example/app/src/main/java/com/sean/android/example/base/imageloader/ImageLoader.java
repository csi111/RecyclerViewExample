package com.sean.android.example.base.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.sean.android.example.base.imageloader.cache.LruMemoryCache;
import com.sean.android.example.base.imageloader.cache.MemoryCache;
import com.sean.android.example.base.imageloader.cache.disk.DiskCache;
import com.sean.android.example.base.imageloader.cache.disk.LruDiskCache;

import java.io.File;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageLoader {

    private ImageSize maxImageSize;

    private ImageLoadingListener defaultImageListener;

    private MemoryCache memoryCache;
    private DiskCache diskCache;

    private ImageLoadExecutor imageLoadExecutor;


    private ImageLoader() {
        defaultImageListener = new DefaultImageLoadingListener();
    }

    public static ImageLoader getInstance() {
        return ImageLoaderHolder.instance;
    }

    private static class ImageLoaderHolder {
        public static final ImageLoader instance = new ImageLoader();
    }

    public synchronized void init(Context context) {
        if (memoryCache == null) {
            memoryCache = new LruMemoryCache();
        }

        if (diskCache == null) {
            File diskCacheDirectory = StorageUtil.getDiskCacheDirectory(context);
            diskCache = new LruDiskCache(diskCacheDirectory);
        }

        imageLoadExecutor = new ImageLoadExecutor(memoryCache, diskCache);
        maxImageSize = getMaxImageSize(context);
    }


    public void loadImage(String uri, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        loadImage(uri, new ImageViewWrapper(imageView), imageLoadingListener);

    }

    public void loadImage(String uri, ImageViewWrapper imageViewWrapper, ImageLoadingListener imageLoadingListener) {
        loadImage(uri, imageViewWrapper, null, imageLoadingListener);
    }

    public void loadImage(String uri, ImageViewWrapper imageViewWrapper, ImageSize imageSize, ImageLoadingListener imageLoadingListener) {
        if (imageViewWrapper == null) {
            throw new IllegalArgumentException("ImageView reference must not be null");
        }

        if (imageLoadingListener == null) {
            imageLoadingListener = defaultImageListener;
        }


        if (TextUtils.isEmpty(uri)) {
            //TODO Cancel Image Load
            imageLoadExecutor.cancelShowImageTask(imageViewWrapper);
            imageLoadingListener.onLoadingStarted(uri);

            imageLoadingListener.onLoadingComplete(uri, imageViewWrapper.getWrappedView(), null);
        }

        if (imageSize == null) {
            imageSize = defineTargetSizeForView(imageViewWrapper, maxImageSize);
        }

        String memoryCacheKey = generateKey(uri, imageSize);

        imageLoadExecutor.prepareShowImageTask(imageViewWrapper, memoryCacheKey);

        imageLoadingListener.onLoadingStarted(uri);

        Bitmap bitmap = memoryCache.get(memoryCacheKey);

        ImageInfo imageInfo = new ImageInfo(uri, memoryCacheKey, imageViewWrapper, imageLoadingListener, imageSize, imageLoadExecutor.getLockForUri(uri));
        if(bitmap != null && !bitmap.isRecycled()) {
            //TODO Bitmap From MemoryCache
            DisplayImageTask displayImageTask = new DisplayImageTask(bitmap, imageInfo, imageLoadExecutor);
            imageLoadExecutor.submit(displayImageTask);
        } else {
            //TODO Bitmap From DiskCache or Network
            LoadImageTask loadImageTask = new LoadImageTask(imageLoadExecutor, imageInfo, getHandler());
            imageLoadExecutor.submit(loadImageTask);
        }

    }

    public void clearMemoryCache() {
        memoryCache.clear();
    }

    public void clearDiskCache() {
        diskCache.clear();
    }

    public void cancelImageTask(ImageViewWrapper imageViewWrapper) {
        imageLoadExecutor.cancelShowImageTask(imageViewWrapper);
    }

    public void cancelImageTask(ImageView imageView) {
        cancelImageTask(new ImageViewWrapper(imageView));
    }

    public void pause() {
        imageLoadExecutor.pause();
    }

    public void resume() {
        imageLoadExecutor.resume();
    }

    public void stop() {
        imageLoadExecutor.stop();
    }

    public void destroy() {
        diskCache.close();
        imageLoadExecutor = null;
    }

    private Handler getHandler() {
        Handler handler = null;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            handler = new Handler();
        }

        return handler;
    }


    private ImageSize defineTargetSizeForView(ImageViewWrapper imageViewWrapper, ImageSize maxImageSize) {
        int width = imageViewWrapper.getWidth();
        if (width <= 0) width = maxImageSize.getWidth();

        int height = imageViewWrapper.getHeight();
        if (height <= 0) height = maxImageSize.getHeight();

        return new ImageSize(width, height);
    }

    private String generateKey(String imageUri, ImageSize targetSize) {
        return new StringBuilder(imageUri).append("-").append(targetSize.getWidth()).append("x").append(targetSize.getHeight()).toString();
    }

    private ImageSize getMaxImageSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return new ImageSize(width, height);
    }
}
