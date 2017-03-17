package com.sean.android.example.base.asynctask;

import com.sean.android.example.base.protocol.ConnectException;
import com.sean.android.example.base.protocol.HttpRequest;
import com.sean.android.example.base.protocol.data.ResponseData;
import com.sean.android.example.base.protocol.UrlConnectionClient;

import java.util.concurrent.Callable;

/**
 * Created by Seonil on 2017-03-09.
 */

public class HttpBackgroundWork<RESPONSE_DTO> implements Callable<HttpBackgroundResult> {
    private HttpRequest httpRequest;
    private String host;
    private String path;

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
        prepareUrlAddress();
    }

    protected void initHttpTransaction() throws ConnectException {
        if (httpRequest == null) {
            httpRequest = new HttpRequest.Builder().baseUrl(buildUrl()).setClient(new UrlConnectionClient()).build();
        }
    }

    protected ResponseData executeHttpTransaction() throws ConnectException {
        return httpRequest.execute();
    }


    protected void onInitHttpTransaction(HttpRequest httpRequest) {
    }

    protected void onBeginHttpTransaction(HttpRequest httpRequest) {
    }

    protected HttpBackgroundResult<RESPONSE_DTO> onCompleteHttpTransation(HttpRequest httpRequest, ResponseData responseData) {
        HttpBackgroundResult httpBackgroundResult = new HttpBackgroundResult(responseData);
        return httpBackgroundResult;
    }

    protected void prepareUrlAddress() {
        HttpUrlPath httpUrlPath = getClass().getAnnotation(HttpUrlPath.class);

        if (httpUrlPath == null || httpUrlPath.host() == null || httpUrlPath.host().length() == 0) {
            return;
        }
        host = httpUrlPath.host();
        path = httpUrlPath.path();
    }

    protected String buildUrl() throws ConnectException {
        if (host == null || host.length() == 0) {
            throw new ConnectException("URL must not be null!!!");
        }

        StringBuilder stringBuilder = new StringBuilder(host);
        stringBuilder.append(path);

        return stringBuilder.toString();
    }

    protected boolean isEmptyUrl() {
        return host == null || host.length() == 0;
    }

    protected String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }
}
