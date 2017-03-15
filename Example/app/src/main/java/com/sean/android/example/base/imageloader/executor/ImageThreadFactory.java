package com.sean.android.example.base.imageloader.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Seonil on 2017-03-13.
 */

public class ImageThreadFactory implements ThreadFactory {

    private static final AtomicInteger threadPoolNumber = new AtomicInteger(1);

    private final int threadPriority;
    private ThreadGroup threadGroup;

    private final String threadName;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable runnable) {

        StringBuilder stringBuilder = new StringBuilder(threadName);
        stringBuilder.append(threadNumber.getAndIncrement());

        Thread thread = new Thread(threadGroup, runnable);

        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }

        thread.setPriority(threadPriority);
        return thread;
    }


    public ImageThreadFactory(int threadPriority, String threadName) {
        this.threadPriority = threadPriority;
        this.threadName = threadName + threadPoolNumber.getAndIncrement() + "_thread";
        threadGroup = Thread.currentThread().getThreadGroup();
    }
}
