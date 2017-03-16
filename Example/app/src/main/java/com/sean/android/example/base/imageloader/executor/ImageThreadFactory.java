package com.sean.android.example.base.imageloader.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageThreadFactory implements ThreadFactory {

    private final int threadPriority;
    private ThreadGroup threadGroup;

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(threadGroup, runnable);

        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }

        thread.setPriority(threadPriority);
        return thread;
    }


    public ImageThreadFactory(int threadPriority) {
        this.threadPriority = threadPriority;
        threadGroup = Thread.currentThread().getThreadGroup();
    }
}
