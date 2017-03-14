package com.sean.android.example.base.imageloader.cache.disk;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Seonil on 2017-03-13.
 */

public class LruDiskCache implements DiskCache {

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    private static final int DISK_CACHE_COUNT = Integer.MAX_VALUE;
    private DiskLruCache lruDiskCache;

    private File diskCacheDir;

    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    private static final int DEFAULT_COMPRESS_QUALITY = 100;
    private static final int DEFAULT_BUFFER_SIZE = 32 * 1024;
    private static final int DEFAULT_IMAGE_TOTAL_SIZE = 500 * 1024;
    private static final int FORCE_CACHED_PERCENTAGE = 90;


    private int bufferSize = DEFAULT_BUFFER_SIZE;

    private Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
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
    public boolean save(String imageUri, InputStream imageStream, DiskCopyListener diskCopyListener) throws IOException {
        DiskLruCache.Editor editor = lruDiskCache.edit(getKey(imageUri));

        if (editor != null) {
            OutputStream outputStream = new BufferedOutputStream(editor.newOutputStream(0), bufferSize);
            boolean isCopied = false;
            try {
                isCopied = copyStream(imageStream, outputStream, diskCopyListener, bufferSize);
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
    public boolean save(String imageUri, Bitmap bitmap, DiskCopyListener diskCopyListener) throws IOException {
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


    private static boolean copyStream(InputStream is, OutputStream os, DiskCopyListener listener, int bufferSize) throws IOException {
        int current = 0;
        int total = is.available();
        if (total <= 0) {
            total = DEFAULT_IMAGE_TOTAL_SIZE;
        }

        final byte[] bytes = new byte[bufferSize];
        int count;
        if (shouldStopLoading(listener, current, total)) return false;
        while ((count = is.read(bytes, 0, bufferSize)) != -1) {
            os.write(bytes, 0, count);
            current += count;
            if (shouldStopLoading(listener, current, total)) return false;
        }
        os.flush();
        return true;
    }

    private static boolean shouldStopLoading(DiskCopyListener listener, int current, int total) {
        if (listener != null) {
            boolean shouldContinue = listener.bytesCopied(current, total);
            if (!shouldContinue) {
                if ((100 * current / total) < FORCE_CACHED_PERCENTAGE) {
                    return true;
                }
            }
        }
        return false;
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setCompressFormat(Bitmap.CompressFormat defaultCompressFormat) {
        this.compressFormat = defaultCompressFormat;
    }

    public void setCompressQuality(int compressQuality) {
        this.compressQuality = compressQuality;
    }

    public static interface DiskCopyListener {
        boolean bytesCopied(int current, int total);
    }

}
