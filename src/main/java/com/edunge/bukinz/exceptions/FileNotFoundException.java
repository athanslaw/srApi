package com.edunge.bukinz.exceptions;

public class FileNotFoundException extends FileException {
    private final String code = "09";
    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return code;
    }
}
