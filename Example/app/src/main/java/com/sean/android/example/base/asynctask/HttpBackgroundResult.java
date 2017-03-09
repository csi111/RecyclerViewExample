package com.sean.android.example.base.asynctask;

import com.sean.android.example.base.protocol.ResponseData;

/**
 * Created by Seonil on 2017-03-09.
 */

public class HttpBackgroundResult<T> {
    private ResponseData responseData;

    private T data;

    public HttpBackgroundResult(ResponseData responseData) {
        this.responseData = responseData;
    }

    public T getData() {
        return data;
    }
}
