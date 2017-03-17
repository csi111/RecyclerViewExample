package com.sean.android.example.base.protocol;

import com.sean.android.example.base.protocol.data.RequestData;
import com.sean.android.example.base.protocol.data.ResponseData;

/**
 * Created by sean on 2017. 3. 9..
 */

public interface Client {

    ResponseData call(RequestData requestData) throws ConnectException;

}
