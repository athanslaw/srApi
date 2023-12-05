package com.edunge.srtool.model;

import java.util.HashMap;
import java.util.Map;

public class TerritorialDataCount {
    private static Map<String, Long> data = new HashMap<>();

    public static void set(String territorialLabel, long count) {
        data.put(territorialLabel, count);
    }

    public static Long get(String territorialLabel){
        try {
            long value = data.get(territorialLabel);
            return value;
        }catch (NullPointerException e){
            return -1L;
        }
    }
}
