package com.edunge.srtool.exceptions;


public class BadRequestException extends BaseException {
    private static String CODE= "05";
    public BadRequestException(String message) {
        super(CODE, message);
    }
}
