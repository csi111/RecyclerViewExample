package com.sean.android.example.base.imageloader.decoder;

import android.graphics.Bitmap;

import com.sean.android.example.base.imageloader.ImageSize;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seonil on 2017-03-15.
 * Do not use
 */

public class DefaultImageDecoder extends ImageDecoder {

    @Override
    public Bitmap decode(String imageUri, ImageSize imageResize) throws IOException {
        return null;
    }

    @Override
    protected InputStream getStream(String imageUri) {
        return null;
    }
}
