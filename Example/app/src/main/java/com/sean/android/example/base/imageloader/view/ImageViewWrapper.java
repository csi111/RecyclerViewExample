package com.sean.android.example.base.imageloader.view;

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

public class ImageViewWrapper implements ViewWrapper<ImageView>{
    private Reference<ImageView> imageViewReference;

    public ImageViewWrapper(ImageView imageView) {
        if (imageView == null) {
            throw new IllegalArgumentException("Imageview must not be null");
        }

        this.imageViewReference = new WeakReference<>(imageView);
    }

    @Override
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

    @Override
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


    @Override
    public ImageView getWrappedView() {
        return imageViewReference.get();
    }

    @Override
    public boolean isGarbageCollected() {
        return imageViewReference.get() == null;
    }

    @Override
    public int getId() {
        ImageView imageView = imageViewReference.get();
        return imageView == null ? super.hashCode() : imageView.hashCode();
    }

    private int getImageViewFieldValue(Object object, String fieldName) {
        // getMaxWidth(), getMaxHeight() required os version 16 current min os version 15
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
