package com.sean.android.example.base.asynctask;

import android.content.Context;

import com.sean.android.example.base.view.GlobalProgressView;

import java.util.List;

/**
 * Created by sean on 2017. 3. 11..
 */

public class BackgroundWorkEventListenerIndicatorProxy extends BackgroundWorkEventListenerProxy{
    private Context context;
    private boolean isShowIndicator;

    public BackgroundWorkEventListenerIndicatorProxy(BackgroundWorker.BackgroundWorkEventListener backgroundWorkEventListener, Context context) {
        this(backgroundWorkEventListener, context, true);
    }

    public BackgroundWorkEventListenerIndicatorProxy(BackgroundWorker.BackgroundWorkEventListener backgroundWorkEventListener, Context context, boolean isShowIndicator) {
        super(backgroundWorkEventListener);
        this.context = context;
        this.isShowIndicator = isShowIndicator;
    }

    @Override
    public void onPreExecute(int transactionId) {
        if(isShowIndicator) {
            GlobalProgressView.show(context);
        }
        super.onPreExecute(transactionId);
    }

    @Override
    public void onComplete(int transactionId, List<BackgroundWorker.BackgroundWorkResult> results) {
        GlobalProgressView.hide();

        super.onComplete(transactionId,results);
    }

    @Override
    public void onError(int transactionId, int backgroundWorkId, Exception e) {
        GlobalProgressView.hide();

        super.onError(transactionId, backgroundWorkId, e);
    }
}
