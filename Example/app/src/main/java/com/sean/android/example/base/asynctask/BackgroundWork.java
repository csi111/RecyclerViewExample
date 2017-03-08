package com.sean.android.example.base.asynctask;

import java.util.concurrent.Callable;

/**
 * Created by sean on 2017. 3. 9..
 */

public class BackgroundWork<Result> {
    private int id;
    private Callable<Result> callable;

    public BackgroundWork(int id, Callable<Result> callable) {
        this.id = id;
        this.callable = callable;
    }

    public int getId() {
        return id;
    }

    public Result run() throws Exception {
        return callable.call();
    }
}
