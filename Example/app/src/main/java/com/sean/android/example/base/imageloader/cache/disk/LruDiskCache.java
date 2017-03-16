package com.sean.android.example.base.imageloader.cache.disk;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Seonil on 2017-03-13.
 */

public class LruDiskCache implements ImageDiskCache {

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    private static final int DISK_CACHE_COUNT = Integer.MAX_VALUE;
    private DiskLruCache lruDiskCache;
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    private static final int DEFAULT_COMPRESS_QUALITY = 100;
    private static final int DEFAULT_BUFFER_SIZE = 32 * 1024;

    private File diskCacheDir;

    private Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
    private int bufferSize = DEFAULT_BUFFER_SIZE;
    private int compressQuality = DEFAULT_COMPRESS_QUALITY;
    private int diskCacheSize = DISK_CACHE_SIZE;
    private int diskCacheCount = DISK_CACHE_COUNT;


    public LruDiskCache(File cacheDir) {
        diskCacheDir = cacheDir;
        initCache();
    }


    private void initCache() {
        try {
            lruDiskCache = DiskLruCache.open(diskCacheDir, 1, 1, diskCacheSize, diskCacheCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File get(String imageUri) {

        DiskLruCache.Snapshot snapshot = null;

        try {
            snapshot = lruDiskCache.get(getKey(imageUri));
            return snapshot == null ? null : snapshot.getFile(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
    }

    @Override
    public void put(String imageUri, Bitmap bitmap) {
        try {
            save(imageUri, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean save(String imageUri, InputStream imageStream) throws IOException {
        DiskLruCache.Editor editor = lruDiskCache.edit(getKey(imageUri));

        if (editor != null) {
            OutputStream outputStream = new BufferedOutputStream(editor.newOutputStream(0), bufferSize);
            boolean isCopied = false;
            try {
                isCopied = copyStream(imageStream, outputStream, bufferSize);
            } finally {
                outputStream.close();

                if (isCopied) {
                    editor.commit();
                } else {
                    editor.abort();
                }

                return isCopied;
            }
        }
        return false;
    }

    @Override
    public boolean save(String imageUri, Bitmap bitmap) throws IOException {
        DiskLruCache.Editor editor = lruDiskCache.edit(getKey(imageUri));
        OutputStream outputStream = new BufferedOutputStream(editor.newOutputStream(0), bufferSize);
        if (editor != null) {
            boolean isSaved = false;
            try {
                isSaved = bitmap.compress(compressFormat, compressQuality, outputStream);
            } finally {
                outputStream.close();

                if (isSaved) {
                    editor.commit();
                } else {
                    editor.abort();
                }

                return isSaved;
            }
        }
        return false;
    }

    @Override
    public void remove(String imageUri) {
        try {
            lruDiskCache.remove(getKey(imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            lruDiskCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lruDiskCache = null;
    }

    @Override
    public void clear() {
        try {
            lruDiskCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            initCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getKey(String imageUri) {
        return String.valueOf(imageUri.hashCode());
    }


    private boolean copyStream(InputStream is, OutputStream os, int bufferSize) throws IOException {
        final byte[] bytes = new byte[bufferSize];
        int count;
        while ((count = is.read(bytes, 0, bufferSize)) != -1) {
            os.write(bytes, 0, count);
        }
        os.flush();
        return true;
    }

    public void setCompressQuality(int compressQuality) {
        this.compressQuality = compressQuality;
    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
    }
}
