package com.example.coolparking.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LoginExceptionHandler {
    //拦截登录异常
    @ExceptionHandler(value=AuthorizeException.class)
    public String handlerAuthorizeException(){
        return "redirect:/pservice/plogin";
    }
}
