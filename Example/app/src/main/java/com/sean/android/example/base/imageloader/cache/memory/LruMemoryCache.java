package com.sean.android.example.base.imageloader.cache.memory;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Seonil on 2017-03-13.
 */

public class LruMemoryCache implements ImageMemoryCache {


    private static final int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);

    private static final int CACHE_SIZE = MAX_MEMORY / 8;

    private LruCache<String, Bitmap> bitmapLruCache;

    public LruMemoryCache() {
        bitmapLruCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    @Override
    public void put(String key, Bitmap value) {
        if (key == null || value == null) {
            throw new NullPointerException("key & value must not be null!!!");
        }

        synchronized (this) {
            if (bitmapLruCache.get(key) == null) {
                bitmapLruCache.put(key, value);
            }
        }
    }

    @Override
    public Bitmap get(String key) {
        if (key == null) {
            throw new NullPointerException("key must not be null!!!");
        }

        synchronized (this) {
            return bitmapLruCache.get(key);
        }
    }

    @Override
    public void remove(String key) {
        if (key == null) {
            throw new NullPointerException("key must not be null!!!");
        }
        bitmapLruCache.remove(key);
    }

    @Override
    public void close() {
        //Do nothing
    }

    @Override
    public void clear() {
        bitmapLruCache.evictAll();
    }
}
