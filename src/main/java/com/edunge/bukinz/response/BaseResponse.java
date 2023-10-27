package com.edunge.bukinz.response;


public class BaseResponse {
    private String statusCode;
    private String statusMessage;

    public BaseResponse(String code, String message) {
        this.statusCode = code;
        this.statusMessage = message;
    }

    public BaseResponse() {
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
