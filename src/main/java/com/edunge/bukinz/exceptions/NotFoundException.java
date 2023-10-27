package com.edunge.bukinz.exceptions;

/**
 * Created by adewale adeleye on 04/10/2019
 **/
public class NotFoundException extends BaseException {
    public static final String CODE= "04";
    public NotFoundException(String message) {
        super(CODE, message);
    }
}
