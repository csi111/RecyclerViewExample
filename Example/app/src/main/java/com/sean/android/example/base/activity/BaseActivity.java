package com.sean.android.example.base.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.sean.android.example.base.asynctask.BackgroundWorkEventListenerIndicatorProxy;
import com.sean.android.example.base.asynctask.BackgroundWorker;
import com.sean.android.example.base.protocol.ConnectException;
import com.sean.android.example.base.util.Logger;
import com.sean.android.example.base.util.ToastMaker;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by sean on 2017. 3. 11..
 */

public abstract class BaseActivity extends AppCompatActivity {

    private BackgroundWorker backgroundWorker = new BackgroundWorker();


    protected boolean isRunningBackgroundWork() {
        return backgroundWorker.isRunning();
    }

    protected boolean hasBackgroundWork() {
        return backgroundWorker.has();
    }

    protected <T> void addBackgroundWork(Callable<T> callable) {
        backgroundWorker.addBackgroundWork(callable);
    }

    protected <T> void addBackgroundWork(int id, Callable<T> callable) {
        backgroundWorker.addBackgroundWork(id, callable);
    }

    protected void executeBackgroundWork() {
        backgroundWorker.execute(0, new BackgroundWorkDefaultEventListener(this));
    }

    protected void executeBackgroundWork(int transactionId) {
        backgroundWorker.execute(transactionId, new BackgroundWorkDefaultEventListener(this));
    }

    protected void executeBackgroundWork(BackgroundWorker.BackgroundWorkEventListener eventListener) {
        backgroundWorker.execute(0, new BackgroundWorkDefaultEventListener(eventListener, this));
    }

    protected void executeBackgroundWork(int transactionId, BackgroundWorker.BackgroundWorkEventListener eventListener) {
        backgroundWorker.execute(transactionId, new BackgroundWorkDefaultEventListener(eventListener, this));
    }

    protected <T> void executeBackgroundWork(Callable<T> callable) {
        executeBackgroundWork(0, callable, null);
    }

    protected <T> void executeBackgroundWork(int transactionId, Callable<T> callable) {
        executeBackgroundWork(transactionId, callable, null);
    }

    protected <T> void executeBackgroundWork(Callable<T> callable, BackgroundWorker.BackgroundWorkEventListener eventListener) {
        executeBackgroundWork(0, callable, eventListener);
    }

    protected <T> void executeBackgroundWork(int transactionId, Callable<T> callable, BackgroundWorker.BackgroundWorkEventListener eventListener) {
        backgroundWorker.addBackgroundWork(callable);
        backgroundWorker.execute(transactionId, new BackgroundWorkDefaultEventListener(eventListener, this));
    }

    protected void onBackgroundWorkPreExecute(int transactionId) {
        Logger.d(this, "onBackgroundWorkPreExecute Stub");
    }

    protected void onBackgroundWorkError(int transactionId, int backgroundWorkId, Exception e) {
        if (!(e instanceof ConnectException) || ((ConnectException) e).isShowAlertMessage()) {
            ToastMaker.makeLongToast(this, e.getMessage());
        }
    }

    protected void onBackgroundWorkComplete(int transactionId, List<BackgroundWorker.BackgroundWorkResult> results) {
    }


    private class BackgroundWorkDefaultEventListener extends BackgroundWorkEventListenerIndicatorProxy {
        public BackgroundWorkDefaultEventListener(Context context) {
            super(null, context);
        }

        public BackgroundWorkDefaultEventListener(BackgroundWorker.BackgroundWorkEventListener backgroundWorkEventListener, Context context) {
            super(backgroundWorkEventListener, context);
        }

        public BackgroundWorkDefaultEventListener(BackgroundWorker.BackgroundWorkEventListener backgroundWorkEventListener, Context context, boolean isShowIndicator) {
            super(backgroundWorkEventListener, context, isShowIndicator);
        }

        @Override
        public void onPreExecute(int transactionId) {
            super.onPreExecute(transactionId);
            if (isNull()) {
                onBackgroundWorkPreExecute(transactionId);
            }
        }

        @Override
        public void onError(int transactionId, int backgroundWorkId, Exception e) {
            super.onError(transactionId, backgroundWorkId, e);
            if (isNull()) {
                onBackgroundWorkError(transactionId, backgroundWorkId, e);
            }
        }

        @Override
        public void onComplete(int transactionId, List<BackgroundWorker.BackgroundWorkResult> results) {
            super.onComplete(transactionId, results);
            if (isNull()) {
                onBackgroundWorkComplete(transactionId, results);
            }
        }
    }


}
