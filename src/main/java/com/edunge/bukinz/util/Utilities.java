package com.edunge.bukinz.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class Utilities {

    private static final String OTP_CHARS = "0123456789";
    private static final int OTP_LENGTH = 6;

    public static boolean dateDifference(LocalDateTime date, int interval){
        LocalDateTime now = LocalDateTime.now();
        long diff = ChronoUnit.MINUTES.between(date, now);
        return diff < interval;
    }
    public static String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(OTP_CHARS.charAt(random.nextInt(OTP_CHARS.length())));
        }

        return otp.toString();
    }
    public static String[] electionTypeArray = new String[]{"","Gubernatorial","Presidential","Senatorial",
            "Federal House of Representative","State House of Assembly","LGA Chairmanship","Councilorship"};
}
