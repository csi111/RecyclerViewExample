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

    public ImageSize scaleDown(int sampleSize) {
        return new ImageSize(width / sampleSize, height / sampleSize);
    }

    public ImageSize scale(float scale) {
        return new ImageSize((int) (width * scale), (int) (height * scale));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(width).append("x").append(height);

        return stringBuilder.toString();
    }
}
