package com.example.coolparking.controller;

import com.example.coolparking.dataobject.ParkingOrder;
import com.example.coolparking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("uservice")
public class UserController {
    @Autowired
    UserService userService;
    @RequestMapping("uorder")
    @ResponseBody
    public Map findOrderById(String openId){
        System.out.println(openId);
        Map<String,Object> map=new HashMap<>();
        map.put("list",userService.findOrderById(openId));
        return map;
    }
}
