package com.sean.android.example.base.imageloader;

/**
 * Created by Seonil on 2017-03-14.
 */

public class ImageSize {

    private final int width;
    private final int height;

    public ImageSize(int size) {
        this(size, size);
    }

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(width).append("x").append(height);

        return stringBuilder.toString();
    }
}
