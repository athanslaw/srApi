package com.edunge.srtool.exceptions;

/**
 * Created by adewale adeleye on 24/10/2019
 **/
public class NotificationException extends BaseException {
    public NotificationException(String statusCode, String statusMessage) {
        super(statusCode,statusMessage);

    }
}
