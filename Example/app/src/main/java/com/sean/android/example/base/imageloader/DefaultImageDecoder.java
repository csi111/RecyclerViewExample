package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seonil on 2017-03-14.
 */

public class DefaultImageDecoder implements ImageDecoder {

    private static final int BUFFER_SIZE = 32 * 1024;

    @Override
    public Bitmap decode(ImageDecodingInfo imageDecodingInfo) throws IOException {
        Bitmap decodeBitmap;

        InputStream inputStream = getStreamFromFile(imageDecodingInfo.imageUri);

        if (inputStream == null) {
            return null;
        }

        try {
            ImageSize imageResize = defineImageSize(inputStream, imageDecodingInfo);
            inputStream = resetStream(inputStream, imageDecodingInfo);
            BitmapFactory.Options options = getDecodingOptions(imageResize, imageDecodingInfo);
            decodeBitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } finally {
            inputStream.close();
        }

        return decodeBitmap;
    }

    protected BitmapFactory.Options getDecodingOptions(ImageSize imageSize, ImageDecodingInfo decodingInfo) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize(options, imageSize.getWidth(), imageSize.getHeight());
        return options;
    }

    private InputStream resetStream(InputStream imageStream, ImageDecodingInfo imageDecodingInfo) throws IOException {
        if (imageStream.markSupported()) {
            try {
                imageStream.reset();
                return imageStream;
            } catch (IOException ignored) {
            }
        }
        imageStream.close();
        return getStreamFromFile(imageDecodingInfo.imageUri);
    }

    private InputStream getStreamFromFile(String imageUri) throws IOException {
        String filePath = StorageUtil.getFilePathFromUri(imageUri);

        BufferedInputStream imageStream = new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE);
        return new ContentLengthInputStream(imageStream, (int) new File(filePath).length());
    }

    protected ImageSize defineImageSize(InputStream imageStream, ImageDecodingInfo decodingInfo) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);

        return new ImageSize(options.outWidth, options.outHeight);
    }


    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
}
