package com.edunge.bukinz.exceptions;

/**
 * Created by adewale adeleye on 12/10/2019
 **/
public class ExceededRetryException extends BaseException {
    private static final String statusCode = "08";
    public ExceededRetryException(String message) {
        super(statusCode,message);
    }
}
