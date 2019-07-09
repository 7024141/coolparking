package com.example.coolparking.aspect;

import com.example.coolparking.dataobject.UserToken;
import com.example.coolparking.exception.AuthorizeException;
import com.example.coolparking.service.UserTokenService;
import com.example.coolparking.utils.CookieUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthorizeAspect {
    public static String token = "";
    public static String UUID = "";

    @Autowired
    UserTokenService userTokenService;

    @Pointcut("!execution(public * com.example.coolparking.controller.ParkingController.parkingToLogin(..))"+
            "&& execution(public * com.example.coolparking.controller.ParkingController.*(..))")
    public void verify(){}

    @Before("verify()")
    public void doVerify(){
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Cookie cookie = CookieUtil.get(request, token);

        //查询value
        UserToken userToken = userTokenService.findOne(UUID);
        if(cookie == null || userToken.isState()){
            throw  new AuthorizeException();
        }
    }
}
