package com.edunge.bukinz.exceptions;

/**
 * Created by adewale adeleye on 31/10/2019
 **/
public class DeviceNotFoundException extends BaseException {
    public DeviceNotFoundException(String message) {
        super("21",message);
    }
}
