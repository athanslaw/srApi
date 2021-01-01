package com.progressoft.task.exception;

public class FileException extends RuntimeException{
    private final String code = "06";
    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }
}
