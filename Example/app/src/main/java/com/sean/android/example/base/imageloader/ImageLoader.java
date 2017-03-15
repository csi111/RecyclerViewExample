package com.sean.android.example.base.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.sean.android.example.base.imageloader.cache.ImageCache;
import com.sean.android.example.base.imageloader.executor.ImageLoadExecutor;
import com.sean.android.example.base.imageloader.task.DisplayImageTask;
import com.sean.android.example.base.imageloader.task.LoadImageTask;
import com.sean.android.example.base.imageloader.view.ImageViewWrapper;
import com.sean.android.example.base.imageloader.view.ViewWrapper;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageLoader {

    private ImageSize maxImageSize;

    private ImageLoadingListener defaultImageListener;

    private ImageCache imageCache;
    private ImageLoadExecutor imageLoadExecutor;

    private Drawable loadingImage = null;
    private Drawable failedImage = null;


    private ImageLoader() {
        imageCache = new ImageCache();
        defaultImageListener = new ImageLoadingListenerProxy();
    }

    public static ImageLoader getInstance() {
        return ImageLoaderHolder.instance;
    }

    private static class ImageLoaderHolder {
        public static final ImageLoader instance = new ImageLoader();
    }

    public synchronized void init(Context context) {
        imageCache.init(context);
        imageLoadExecutor = new ImageLoadExecutor(imageCache);
        maxImageSize = getDefaultMaxImageSize(context);
    }

    public void loadImage(String uri, ImageView imageView) {
        loadImage(uri, new ImageViewWrapper(imageView), new ImageLoadingListenerProxy());
    }


    public void loadImage(String uri, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        loadImage(uri, new ImageViewWrapper(imageView), imageLoadingListener);

    }

    public void loadImage(String uri, ViewWrapper imageViewWrapper, ImageLoadingListener imageLoadingListener) {
        loadImage(uri, imageViewWrapper, null, imageLoadingListener);
    }

    public void loadImage(String uri, final ViewWrapper imageViewWrapper, ImageSize imageSize, ImageLoadingListener imageLoadingListener) {
        if (imageViewWrapper == null) {
            throw new IllegalArgumentException("ImageView reference must not be null");
        }

        if (imageLoadingListener == null) {
            imageLoadingListener = defaultImageListener;
        }


        if (TextUtils.isEmpty(uri)) {
            imageViewWrapper.getWrappedView().post(new Runnable() {
                @Override
                public void run() {
                    ((ImageView) imageViewWrapper.getWrappedView()).setImageDrawable(failedImage);
                }
            });
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


        Bitmap bitmap = imageCache.getBitmapFromMemCache(memoryCacheKey);


        ImageInfo imageInfo = new ImageInfo(uri, memoryCacheKey, imageViewWrapper, imageLoadingListener, imageSize, imageLoadExecutor.getLockForUri(uri));
        if (loadingImage != null) {
            imageViewWrapper.getWrappedView().post(new Runnable() {
                @Override
                public void run() {
                    ((ImageView) imageViewWrapper.getWrappedView()).setImageDrawable(loadingImage);
                }
            });
        }

        if (bitmap != null && !bitmap.isRecycled()) {
            DisplayImageTask displayImageTask = new DisplayImageTask(bitmap, imageInfo, imageLoadExecutor);
            imageLoadExecutor.submit(displayImageTask);
        } else {
            LoadImageTask loadImageTask = new LoadImageTask(imageLoadExecutor, imageInfo, getHandler());
            imageLoadExecutor.submit(loadImageTask);
        }

    }

    public ImageLoader setMaxImageSize(int size) {
        return setMaxImageSize(size, size);
    }

    public ImageLoader setMaxImageSize(int width, int height) {
        maxImageSize = new ImageSize(width, height);
        return this;
    }

    public void clearCache() {
        imageCache.clear();
    }

    public void cancelImageTask(ViewWrapper imageViewWrapper) {
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
        imageCache.close();
        imageLoadExecutor = null;
    }

    public void setLoadingImage(Drawable loadingImage) {
        this.loadingImage = loadingImage;
    }

    public void setFailedImage(Drawable failedImage) {
        this.failedImage = failedImage;
    }

    private Handler getHandler() {
        Handler handler = null;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            handler = new Handler();
        }

        return handler;
    }


    private ImageSize defineTargetSizeForView(ViewWrapper imageViewWrapper, ImageSize maxImageSize) {
        int width = imageViewWrapper.getWidth();
        if (width <= 0) width = maxImageSize.getWidth();

        int height = imageViewWrapper.getHeight();
        if (height <= 0) height = maxImageSize.getHeight();

        return new ImageSize(width, height);
    }

    private String generateKey(String imageUri, ImageSize targetSize) {
        return new StringBuilder(imageUri).append("-").append(targetSize.getWidth()).append("x").append(targetSize.getHeight()).toString();
    }

    private ImageSize getDefaultMaxImageSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return new ImageSize(width, height);
    }
}
