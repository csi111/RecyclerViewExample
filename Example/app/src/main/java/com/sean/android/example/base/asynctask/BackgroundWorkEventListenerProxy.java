package com.sean.android.example.base.asynctask;

import java.util.List;

/**
 * Created by sean on 2017. 3. 11..
 */

public class BackgroundWorkEventListenerProxy implements BackgroundWorker.BackgroundWorkEventListener {

    private BackgroundWorker.BackgroundWorkEventListener backgroundWorkEventListener;

    public BackgroundWorkEventListenerProxy() {
        backgroundWorkEventListener = null;
    }

    public BackgroundWorkEventListenerProxy(BackgroundWorker.BackgroundWorkEventListener backgroundWorkEventListener) {
        this.backgroundWorkEventListener = backgroundWorkEventListener;
    }

    public boolean isNull() {
        return backgroundWorkEventListener == null;
    }

    public boolean isNotNull() {
        return !isNull();
    }


    @Override
    public void onPreExecute(int transactionId) {
        if(isNotNull()) {
            backgroundWorkEventListener.onPreExecute(transactionId);
        }
    }

    @Override
    public void onComplete(int transactionId, List<BackgroundWorker.BackgroundWorkResult> results) {
        if(isNotNull()) {
            backgroundWorkEventListener.onComplete(transactionId, results);
        }
    }

    @Override
    public void onError(int transactionId, int backgroundWorkId, Exception e) {
        if(isNotNull()) {
            backgroundWorkEventListener.onError(transactionId, backgroundWorkId, e);
        }

    }

    @Override
    public void onCanceled(int transactionId) {
        if(isNotNull()){
            backgroundWorkEventListener.onCanceled(transactionId);
        }

    }
}
