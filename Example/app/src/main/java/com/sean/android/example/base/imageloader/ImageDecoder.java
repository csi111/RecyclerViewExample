package com.sean.android.example.base.imageloader;

import android.graphics.Bitmap;

import java.io.IOException;

/**
 * Created by Seonil on 2017-03-13.
 */

public interface ImageDecoder {

    Bitmap decode(ImageDecodingInfo imageDecodingInfo) throws IOException;
}
