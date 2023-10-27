package com.edunge.bukinz.exceptions;

public class MissingFieldsException extends RuntimeException {
    private final String code = "11";
    public MissingFieldsException(String code, Throwable message) {
        super(code, message);
    }

    public MissingFieldsException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }
}
