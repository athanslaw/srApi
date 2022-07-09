package com.edunge.srtool.service.impl;

import com.edunge.srtool.service.CallService;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.net.URISyntaxException;


@Service
public class CallServiceImpl implements CallService {
    public static final String ACCOUNT_SID = "AC5b600f7ca65984fd3d13388b5b6f94f4";//System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = "5046b1855257b3ececa3cbd68f89c92d"; //System.getenv("TWILIO_AUTH_TOKEN");
    public static final String twilioPhoneNumber = "+17652689081";

    public void makeCall(String phoneNo) throws URISyntaxException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String from = twilioPhoneNumber;
        String to = phoneNo;

        Call call = Call.creator(new PhoneNumber(to), new PhoneNumber(from),
                new URI("http://demo.twilio.com/docs/voice.xml")).create();

        System.out.println(call.getSid());
    }
}
