package com.sean.android.example.base.protocol;

/**
 * Created by sean on 2017. 3. 9..
 */

public interface Client {

    ResponseData call(RequestData requestData) throws ConnectException;

}
