package com.sean.android.example.base.imageloader.decoder;

import com.sean.android.example.base.imageloader.ImageSize;

/**
 * Created by Seonil on 2017-03-14.
 */

public class ImageDecodingInfo {
    private final String imageKey;
    private final String imageUri;
    private final String originalImageUri;
    private final ImageSize imageSize;


    public ImageDecodingInfo(String imageKey, String imageUri, String originalImageUri, ImageSize imageSize) {
        this.imageKey = imageKey;
        this.imageUri = imageUri;
        this.originalImageUri = originalImageUri;
        this.imageSize = imageSize;
    }
}
