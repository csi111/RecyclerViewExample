package com.sean.android.example.base.asynctask;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by sean on 2017. 3. 9..
 */

public class BackgroundWorker {
    public static final int THREAD_POOL_EXECUTOR = 1;
    public static final int SERIAL_EXECUTOR = 0;
    private static final String TAG = BackgroundWorker.class.getSimpleName();
    private int executorType = SERIAL_EXECUTOR;
    private Queue<BackgroundWork<?>> backgroundWorks;
    private AtomicBoolean isRunning;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.toString());
            if (msg.what == 0) {
                ((BackgroundWorkAsyncTask) msg.obj).execute();
            } else if (msg.what == 1) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    ((BackgroundWorkAsyncTask) msg.obj).execute();
                } else {
                    ((BackgroundWorkAsyncTask) msg.obj).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

            } else if (msg.what == 2) {
                ((BackgroundWorkAsyncTask) msg.obj).cancel(true);
            }
        }
    };

    public BackgroundWorker() {
        backgroundWorks = new LinkedBlockingQueue<BackgroundWork<?>>();
        isRunning = new AtomicBoolean(false);
    }

    public <T> void addBackgroundWork(Callable<T> callable) {
        addBackgroundWork(generateBackgroundWorkId(), callable);
    }

    public <T> void addBackgroundWork(int id, Callable<T> callable) {
        backgroundWorks.add(new BackgroundWork<T>(id, callable));
    }

    public int count() {
        return backgroundWorks.size();
    }

    public boolean isEmpty() {
        return backgroundWorks.isEmpty();
    }

    public boolean has() {
        return !isEmpty();
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    public void execute() {
        execute(generateTransactionId());
    }

    public void execute(int transactionId) {
        execute(transactionId, null);
    }

    public <T> void execute(BackgroundWorkEventListener eventListener) {
        execute(generateTransactionId(), eventListener);
    }

    public <T> void execute(Callable<T> callable) {
        addBackgroundWork(generateBackgroundWorkId(), callable);
        execute(generateTransactionId());
    }

    public <T> void execute(Callable<T> callable, BackgroundWorkEventListener eventListener) {
        addBackgroundWork(generateBackgroundWorkId(), callable);
        execute(generateTransactionId(), eventListener);
    }

    public void execute(int transactionId, BackgroundWorkEventListener eventListener) {
        if (backgroundWorks.isEmpty()) {
            throw new RuntimeException("backgroundWork is empty");
        }

        isRunning.set(true);
        List<BackgroundWork<?>> executeBackgroundWorks = new ArrayList<BackgroundWork<?>>();

        while (!backgroundWorks.isEmpty()) {
            executeBackgroundWorks.add(backgroundWorks.poll());
        }

        if (executorType == THREAD_POOL_EXECUTOR) {
            for (BackgroundWork<?> backgroundWork : executeBackgroundWorks) {
                Message.obtain(handler, executorType, new BackgroundWorkAsyncTask(transactionId, backgroundWork, eventListener)).sendToTarget();
            }
        } else {
            Message.obtain(handler, executorType, new BackgroundWorkAsyncTask(transactionId, executeBackgroundWorks, eventListener)).sendToTarget();
        }
    }

    public void cancel(int transactionId) {
        if (backgroundWorks.isEmpty()) {
            throw new RuntimeException("backgroundWork is empty");
        }
        //TODO 차기버젼 Cancel API 구현 예정
    }

    private int generateTransactionId() {
        return 0;
    }

    private int generateBackgroundWorkId() {
        return 0;
    }

    public void setExecutorType(int executorType) {
        this.executorType = executorType;
    }

    public static interface BackgroundWorkEventListener {
        void onPreExecute(int transactionId);

        void onComplete(int transactionId, List<BackgroundWorkResult> results);

        void onError(int transactionId, int backgroundWorkId, Exception e);

        void onCanceled(int transactionId);
    }

    public static class BackgroundWorkResult {
        private int backgroundWorkId;
        private Object result;
        private boolean isSuccess;

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public int getBackgroundWorkId() {
            return backgroundWorkId;
        }

        public void setBackgroundWorkId(int backgroundWorkId) {
            this.backgroundWorkId = backgroundWorkId;
        }
    }

    private class BackgroundWorkAsyncTask extends AsyncTask<Void, Void, List<BackgroundWorkResult>> {
        private int transactionId;
        private List<BackgroundWork<?>> backgroundWorks;
        private BackgroundWorkEventListener backgroundWorkEventListener;

        private BackgroundWorkAsyncTask(int transactionId, List<BackgroundWork<?>> backgroundWorks, BackgroundWorkEventListener backgroundWorkEventListener) {
            Log.d(TAG, "BackgroundWorkAsyncTask Construct");
            this.transactionId = transactionId;
            this.backgroundWorks = backgroundWorks;
            this.backgroundWorkEventListener = backgroundWorkEventListener;
        }

        private BackgroundWorkAsyncTask(int transactionId, BackgroundWork<?> backgroundWork, BackgroundWorkEventListener backgroundWorkEventListener) {
            Log.d(TAG, "BackgroundWorkAsyncTask Construct");
            this.transactionId = transactionId;
            this.backgroundWorks = new ArrayList<BackgroundWork<?>>();
            this.backgroundWorks.add(backgroundWork);
            this.backgroundWorkEventListener = backgroundWorkEventListener;
        }

        @Override
        protected void onPreExecute() {
            occurEventPreExecute();
        }

        @Override
        protected List<BackgroundWorkResult> doInBackground(Void... params) {
            List<BackgroundWorkResult> results = new ArrayList<BackgroundWorkResult>();
            for (BackgroundWork<?> backgroundWork : backgroundWorks) {
                BackgroundWorkResult result = new BackgroundWorkResult();
                try {
                    result.setBackgroundWorkId(backgroundWork.getId());
                    result.setResult(backgroundWork.run());
                    result.setSuccess(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    result.setResult(e);
                    result.setSuccess(false);
                    break;
                } finally {
                    results.add(result);
                }
            }
            isRunning.set(false);
            return results;
        }

        @Override
        protected void onPostExecute(List<BackgroundWorkResult> results) {
            BackgroundWorkResult failResult = getFailResult(results);
            if (failResult == null) {
                occurEventComplete(results);
            } else {
                occurEventError(failResult.getBackgroundWorkId(), (Exception) failResult.getResult());
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(List<BackgroundWorkResult> results) {
            super.onCancelled(results);
            occurEventCancel(transactionId);
        }

        public BackgroundWorkResult getFailResult(List<BackgroundWorkResult> results) {
            BackgroundWorkResult failResult = null;
            for (BackgroundWorkResult result : results) {
                if (!result.isSuccess()) {
                    failResult = result;
                    break;
                }
            }
            return failResult;
        }

        private void occurEventPreExecute() {
            if (backgroundWorkEventListener != null) {
                backgroundWorkEventListener.onPreExecute(transactionId);
            }
        }

        private void occurEventComplete(List<BackgroundWorkResult> results) {
            if (backgroundWorkEventListener != null) {
                backgroundWorkEventListener.onComplete(transactionId, results);
            }
        }

        private void occurEventError(int backgroundWorkId, Exception e) {
            if (backgroundWorkEventListener != null) {
                backgroundWorkEventListener.onError(transactionId, backgroundWorkId, e);
            }
        }

        private void occurEventCancel(int backgroundWorkId) {
            if (backgroundWorkEventListener != null) {
                backgroundWorkEventListener.onCanceled(transactionId);
            }
        }
    }
}
