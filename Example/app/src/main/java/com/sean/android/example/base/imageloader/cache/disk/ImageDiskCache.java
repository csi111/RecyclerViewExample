package com.sean.android.example.base.imageloader.cache.disk;

import android.graphics.Bitmap;

import com.sean.android.example.base.imageloader.cache.Cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seonil on 2017-03-13.
 */

public interface ImageDiskCache extends Cache<File> {

    boolean save(String imageUri, InputStream imageStream) throws IOException;

    boolean save(String imageUri, Bitmap bitmap) throws IOException;
}
