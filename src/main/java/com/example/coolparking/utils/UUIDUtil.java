package com.example.coolparking.utils;

import java.util.Date;

public class UUIDUtil {
    public static String createUUID(){
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        return timestamp;
    }
}
