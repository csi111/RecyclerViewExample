package com.sean.android.example.base.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Seonil on 2017-03-09.
 */

public final class ResponseData {
    private final int statusCode;

    private final String responseMessage;

    private final List<Header> headers;

    private final Input responseBody;


    public ResponseData(int code, String response, List<Header> headerList, Input body) throws ConnectException {
        if (code < 200 || code > 300) {
            throw new ConnectException(code, response);
        }

        this.statusCode = code;
        this.responseMessage = response;
        this.responseBody = body;
        this.headers = Collections.unmodifiableList(new ArrayList<Header>(headerList));

    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<Header> getHeaders() {
        return headers;
    }
}
