package com.edunge.srtool.exceptions;

public class InvalidFileException extends RuntimeException{
    private final String code = "10";
    public InvalidFileException(String code, Throwable throwable) {
        super(code,throwable );
    }

    public InvalidFileException(String message) {
        super(message);
    }
    public String getCode() {
        return code;
    }
}
