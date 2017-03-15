package com.sean.android.example.base.imageloader.decoder;

import android.graphics.Bitmap;

import com.sean.android.example.base.imageloader.ImageSize;

import java.io.IOException;

/**
 * Created by Seonil on 2017-03-13.
 */

public interface ImageDecoder {

    Bitmap decode(String imageUri, ImageSize imageResize) throws IOException;
}
