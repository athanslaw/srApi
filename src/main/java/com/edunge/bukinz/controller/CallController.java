package com.edunge.bukinz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
public class CallController {

//    @Autowired
//    CallService callService;

    @GetMapping("/call/{phone}")
    public void makeCall(@PathVariable("phone") String phoneNo) throws URISyntaxException {
        //callService.makeCall(phoneNo);
    }
}
