package com.sean.android.example.base.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 2017. 3. 9..
 */

public class UrlConnectionClient implements Client {
    private static final int CHUNK_SIZE = 2048;

    @Override
    public ResponseData call(RequestData requestData) throws ConnectException {
        try {
            HttpURLConnection httpURLConnection = openConnection(requestData);
            prepareCall(httpURLConnection, requestData);
            return readResponse(httpURLConnection);
        } catch (MalformedURLException e) {
            throw new ConnectException(e.getCause());
        } catch (IOException e) {
            throw new ConnectException(e.getCause());
        }
    }

    protected HttpURLConnection openConnection(RequestData requestData) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) requestData.getURL().openConnection();
        httpURLConnection.setConnectTimeout(Defines.CONNECT_TIMEOUT_MILLS);
        httpURLConnection.setReadTimeout(Defines.READ_TIMEOUT_MILLS);
        return httpURLConnection;
    }

    void prepareCall(HttpURLConnection httpURLConnection, RequestData requestData) {

//        httpURLConnection.setDoInput(true); //Default true

        for (Header header : requestData.getHeaders()) {
            httpURLConnection.addRequestProperty(header.getName(), header.getValue());
        }
        httpURLConnection.setChunkedStreamingMode(CHUNK_SIZE);
        try {
            httpURLConnection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

    ResponseData readResponse(HttpURLConnection httpURLConnection) throws IOException, ConnectException {
        int statusCode = httpURLConnection.getResponseCode();
        String reason = httpURLConnection.getResponseMessage();
        List<Header> headers = new ArrayList<Header>();
        for (Map.Entry<String, List<String>> field : httpURLConnection.getHeaderFields().entrySet()) {
            String name = field.getKey();
            for (String value : field.getValue()) {
                headers.add(new Header(name, value));
            }
        }
        return new ResponseData(statusCode, reason, headers, createResponseBody(httpURLConnection));
    }

    private Input createResponseBody(final HttpURLConnection httpURLConnection) {
        return new Input() {
            @Override
            public String getContentType() {
                return httpURLConnection.getContentType();
            }

            @Override
            public InputStream in() throws IOException {
                return httpURLConnection.getInputStream();
            }

            @Override
            public long length() {
                return httpURLConnection.getContentLength();
            }
        };
    }


}
