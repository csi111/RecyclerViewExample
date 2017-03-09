package com.sean.android.example.base.protocol;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Seonil on 2017-03-09.
 */

public class RequestData {
    private final String url;

    private final List<Header> headers;


    public RequestData(String url, List<Header> headerList) {
        if (url == null) {
            throw new NullPointerException("URL must have values");
        }

        this.url = url;


        if (headerList == null) {
            headers = Collections.emptyList();
        } else {
            headers = Collections.unmodifiableList(new ArrayList<Header>(headerList));
        }
    }


    public String getUrl() {
        return url;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public URL getURL() throws MalformedURLException {
        URL urlValue = new URL(url);
        return new URL(url);
    }

}
