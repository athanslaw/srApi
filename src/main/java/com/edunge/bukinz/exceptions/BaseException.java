package com.edunge.bukinz.exceptions;


public class BaseException extends Exception {
    private String statusCode;
    private String statusMessage;

    public BaseException(String code, String message) {
        super(message);
        this.statusCode = code;
        this.statusMessage = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
