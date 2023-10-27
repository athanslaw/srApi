package com.edunge.bukinz.exceptions;

public class DuplicateException extends RuntimeException {
    private final String code;
    private final String message;

    public DuplicateException(String message) {
        super(message);
        this.message = message;
        this.code = "09";
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
