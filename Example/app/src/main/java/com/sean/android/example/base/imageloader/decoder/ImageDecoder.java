package com.sean.android.example.base.imageloader.decoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sean.android.example.base.imageloader.ImageSize;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seonil on 2017-03-13.
 */

public abstract class ImageDecoder {

    protected ImageSize defineImageSize(InputStream imageStream) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);

        return new ImageSize(options.outWidth, options.outHeight);
    }

    protected BitmapFactory.Options getDecodingOptions(ImageSize imageSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize(options, imageSize.getWidth(), imageSize.getHeight());
        return options;
    }

    protected int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            long totalPixels = width * height / inSampleSize;

            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }

    abstract public Bitmap decode(String imageUri, ImageSize imageResize) throws IOException;

    abstract protected InputStream getStream(String imageUri);
}
