package com.sean.android.example.base.imageloader.decoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sean.android.example.base.imageloader.ImageSize;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seonil on 2017-03-15.
 */

public class ImageFileDecoder extends ImageDecoder {

    private static final int BUFFER_SIZE = 32 * 1024; // BufferedInputStream System Buffer Size

    @Override
    public Bitmap decode(String imageUri, ImageSize imageResize) throws IOException {
        Bitmap decodeBitmap;

        InputStream inputStream = getStream(imageUri);

        if (inputStream == null) {
            return null;
        }

        try {
            if (imageResize == null) {
                imageResize = defineImageSize(inputStream);
            }
            inputStream = resetStream(inputStream, imageUri);
            BitmapFactory.Options options = getDecodingOptions(imageResize);
            decodeBitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } finally {
            inputStream.close();
        }

        return decodeBitmap;
    }

    @Override
    protected InputStream getStream(String imageUri) {
        String filePath = ImageType.getPathWithoutScheme(imageUri);

        BufferedInputStream imageStream = null;
        try {
            imageStream = new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imageStream;
    }



    protected InputStream resetStream(InputStream imageStream, String imageUri) throws IOException {
        if (imageStream.markSupported()) {
            try {
                imageStream.reset();
                return imageStream;
            } catch (IOException ignored) {
            }
        }

        imageStream.close();
        return getStream(imageUri);
    }



}
