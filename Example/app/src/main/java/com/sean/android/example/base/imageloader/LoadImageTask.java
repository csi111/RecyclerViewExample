package com.sean.android.example.base.imageloader;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Seonil on 2017-03-13.
 */

public class LoadImageTask implements Runnable {

    ImageLoadExecutor imageLoadExecutor;


    @Override
    public void run() {

    }


    private boolean isPaused() {
        AtomicBoolean pause = imageLoadExecutor.getPause();

        if (pause.get()) {
            synchronized (imageLoadExecutor.getPauseLock()) {
                if (pause.get()) {
                    try {
                        imageLoadExecutor.getPauseLock().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
