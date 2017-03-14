package com.sean.android.example.base.imageloader;

/**
 * Created by Seonil on 2017-03-14.
 */

public class ImageDecodingInfo {
    final String imageKey;
    final String imageUri;
    final String originalImageUri;
    final ImageSize imageSize;


    public ImageDecodingInfo(String imageKey, String imageUri, String originalImageUri, ImageSize imageSize) {
        this.imageKey = imageKey;
        this.imageUri = imageUri;
        this.originalImageUri = originalImageUri;
        this.imageSize = imageSize;
    }
}
