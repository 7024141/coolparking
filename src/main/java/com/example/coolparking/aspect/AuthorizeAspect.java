package com.example.coolparking.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizeAspect {

    @Pointcut("execution(public * com.example.coolparking.controller.ParkingController.*(..))"+
            "&& !execution(public * com.example.coolparking.controller.ParkingController.parkingToLogin())")
    public void verify(){}

    public void doVerify(){

    }
}
