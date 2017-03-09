package com.sean.android.example.base.protocol;

/**
 * Created by Seonil on 2017-03-09.
 */

public class ConnectException extends Exception {

    private int code;
    private boolean isShowAlertMessage;
    private Object object;


    public ConnectException(String message) {
        this(-1, message);
    }

    public ConnectException(int code, String message) {
        this(code, message, null);
    }

    public ConnectException(Throwable throwable) {
        this(-1, "", throwable);
    }

    public ConnectException(String message, Throwable throwable) {
        this(-1, message, throwable);
    }

    public ConnectException(int code, String message, Throwable throwable) {
        this(code, message, true, throwable);
    }

    public ConnectException(int code, String message, boolean isShowAlertMessage) {
        this(code, message, isShowAlertMessage, null);
    }

    public ConnectException(int code, String message, boolean isShowAlertMessage, Object object) {
        this(code, message, isShowAlertMessage, null, object);
    }

    public ConnectException(int code, String message, boolean isShowAlertMessage, Throwable throwable) {
        this(code, message, isShowAlertMessage, throwable, null);
    }

    public ConnectException(int code, String message, boolean isShowAlertMessage, Throwable throwable, Object object) {
        super(message, throwable);
        this.code = code;
        this.isShowAlertMessage = isShowAlertMessage;
        this.object = object;
    }


    public int getCode() {
        return code;
    }

    public boolean isShowAlertMessage() {
        return isShowAlertMessage;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (message == null || message.length() == 0) {
            message = "Failed Connect";
        }
        return message;
    }
}
