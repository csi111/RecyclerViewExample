package com.sean.android.example.base.imageloader.cache.disk;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seonil on 2017-03-13.
 */

public interface DiskCache {

    File get(String imageUri);

    boolean save(String imageUri, InputStream imageStream, LruDiskCache.DiskCopyListener diskCopyListener) throws IOException;

    boolean save(String imageUri, Bitmap bitmap, LruDiskCache.DiskCopyListener diskCopyListener) throws IOException;

    void remove(String imageUri);

    void close();

    void clear();
}
