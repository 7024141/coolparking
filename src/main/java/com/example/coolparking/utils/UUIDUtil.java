package com.example.coolparking.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class UUIDUtil {
    public static String createUUID(){
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        return timestamp;
    }

    public static String getUUID(HttpServletRequest request, String parkingId){
        Cookie cookie = CookieUtil.get(request, parkingId);
        if(cookie != null){
            return cookie.getValue();
        }else return null;
    }
}
