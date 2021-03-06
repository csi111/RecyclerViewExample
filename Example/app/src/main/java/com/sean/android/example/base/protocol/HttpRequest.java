package com.sean.android.example.base.protocol;

import com.sean.android.example.base.protocol.header.Header;
import com.sean.android.example.base.protocol.data.RequestData;
import com.sean.android.example.base.protocol.data.ResponseData;

import java.util.Collections;
import java.util.List;

/**
 * Created by Seonil on 2017-03-09.
 */

public class HttpRequest {

    private final Client client;
    private final RequestData requestData;

    HttpRequest(Client client, RequestData requestData) {
        this.client = client;
        this.requestData = requestData;
    }

    public ResponseData execute() throws ConnectException {
        return client.call(requestData);
    }

    public static final class Builder {
        private Client client;
        private String baseUrl;
        private List<Header> headers;


        public Builder() {

        }

        public Builder setClient(Client client) {
            this.client = client;

            if (client == null) {
                throw new IllegalArgumentException("client== null");
            }
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;

            if (baseUrl == null || baseUrl.length() == 0) {
                throw new IllegalArgumentException("Illegal url =[" + baseUrl + "]");
            }

            return this;
        }

        public Builder addHeaders(List<Header> headerList) {
            if (headers == null) {
                headers = Collections.emptyList();
            }
            headers.addAll(headerList);
            return this;
        }

        public Builder addHeaders(Header header) {
            if (headers == null) {
                headers = Collections.emptyList();
            }

            headers.add(header);
            return this;
        }

        public HttpRequest build() {
            if (baseUrl == null || baseUrl.length() == 0) {
                throw new IllegalStateException("base Url must not be null");
            }

            if (client == null) {
                throw new IllegalStateException("client must not be null");
            }

            RequestData requestData = new RequestData(baseUrl, headers);

            return new HttpRequest(client, requestData);
        }

    }
}
