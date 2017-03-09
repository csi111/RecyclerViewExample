package com.sean.android.example.base.asynctask;

import com.sean.android.example.base.protocol.ConnectException;
import com.sean.android.example.base.protocol.HttpRequest;
import com.sean.android.example.base.protocol.ResponseData;
import com.sean.android.example.base.protocol.UrlConnectionClient;

import java.util.concurrent.Callable;

/**
 * Created by Seonil on 2017-03-09.
 */

public class HttpBackgroundWork<RESPONSE_DTO> implements Callable<HttpBackgroundResult> {
    private HttpRequest httpRequest;
    private String baseUrl;

    public HttpBackgroundWork() {
        this(null);
    }

    public HttpBackgroundWork(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public HttpBackgroundResult<RESPONSE_DTO> call() throws ConnectException {
        preHttpTransaction();
        initHttpTransaction();
        onInitHttpTransaction(httpRequest);
        onBeginHttpTransaction(httpRequest);
        ResponseData httpResponse = executeHttpTransaction();
        HttpBackgroundResult httpBackgroundResult = onCompleteHttpTransation(httpRequest, httpResponse);
        return httpBackgroundResult;
    }

    protected void preHttpTransaction() {
        buildUrl();
    }

    protected void initHttpTransaction() {
        if (httpRequest == null) {
            httpRequest = new HttpRequest.Builder().baseUrl(baseUrl).setClient(new UrlConnectionClient()).build();
        }
    }

    protected ResponseData executeHttpTransaction() throws ConnectException {
        return httpRequest.execute();
    }


    protected void onInitHttpTransaction(HttpRequest httpRequest) {
    }

    protected void onBeginHttpTransaction(HttpRequest httpRequest) {
    }

    protected HttpBackgroundResult onCompleteHttpTransation(HttpRequest httpRequest, ResponseData responseData) {
        return null;
    }

    protected void buildUrl() {
        HttpUrlPath httpUrlPath = getClass().getAnnotation(HttpUrlPath.class);

        if (httpUrlPath != null && httpUrlPath.path() != null && httpUrlPath.path().length() > 0) {
            baseUrl = httpUrlPath.path();
        }
    }

    protected boolean isEmptyUrl() {
        return baseUrl == null || baseUrl.length() == 0;
    }

}
