package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sean.android.example.base.util.Logger;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageViewWrapper {
    private Reference<ImageView> imageViewReference;

    public ImageViewWrapper(ImageView imageView) {
        if (imageView == null) {
            throw new IllegalArgumentException("Imageview must not be null");
        }

        this.imageViewReference = new WeakReference<>(imageView);
    }

    public int getWidth() {
        ImageView imageView = imageViewReference.get();

        if (imageView != null) {
            int width = 0;

            ViewGroup.LayoutParams params = imageView.getLayoutParams();

            if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = imageView.getWidth();
            }

            if (width <= 0 && params != null) {
                width = params.width;
            }

            if (width <= 0) {
                width = getImageViewFieldValue(imageView, "mMaxWidth");
            }
            return width;
        }

        return 0;
    }

    public int getHeight() {
        ImageView imageView = imageViewReference.get();

        if (imageView != null) {
            int height = 0;

            ViewGroup.LayoutParams params = imageView.getLayoutParams();

            if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = imageView.getHeight();
            }

            if (height <= 0 && params != null) {
                height = params.height;
            }

            if (height <= 0) {
                height = getImageViewFieldValue(imageView, "mMaxHeight");
            }
            return height;
        }

        return 0;
    }

    public ImageView getWrappedView() {
        return imageViewReference.get();
    }

    public boolean isGarbageCollected() {
        return imageViewReference.get() == null;
    }

    public int getId() {
        ImageView imageView = imageViewReference.get();
        return imageView == null ? super.hashCode() : imageView.hashCode();
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            ImageView imageView = imageViewReference.get();

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        } else {
            Logger.d(this, "ImageView must set Bitmap on MainThread!!!");
        }
    }

    public void setImageDrawable(Drawable drawable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            ImageView imageView = imageViewReference.get();

            if (imageView != null) {
                imageView.setImageDrawable(drawable);
            }

            if (drawable instanceof AnimationDrawable) {
                ((AnimationDrawable) drawable).start();
            }
        } else {
            Logger.d(this, "ImageView must set Drawable on MainThread!!!");
        }
    }


    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
