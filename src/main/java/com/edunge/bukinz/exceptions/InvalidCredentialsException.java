package com.edunge.bukinz.exceptions;

/**
 * Created by adewale adeleye on 12/10/2019
 **/
public class InvalidCredentialsException extends BaseException {
    private static final String statusCode = "09";
    public InvalidCredentialsException(String message) {
        super(statusCode, message);
    }
}
