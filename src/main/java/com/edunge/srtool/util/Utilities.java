package com.edunge.srtool.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Utilities {
    public static boolean dateDifference(LocalDateTime date, int interval){
        LocalDateTime now = LocalDateTime.now();
        long diff = ChronoUnit.MINUTES.between(date, now);
        return diff < interval;
    }
}
