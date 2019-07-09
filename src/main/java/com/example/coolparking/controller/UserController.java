package com.example.coolparking.controller;

import com.example.coolparking.dataobject.ParkingOrder;
import com.example.coolparking.dataobject.UserInfo;
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


    @RequestMapping("ulogin")
    @ResponseBody
    public String userLogin(String openId){
        userService.login(openId);
        return "success";
    }

    @RequestMapping("ucheckorder")
    @ResponseBody
    public Map findOrderById(String openId){
        System.out.println(openId);
        Map<String,Object> map=new HashMap<>();
        map.put("list",userService.findOrderById(openId));
        return map;
    }

    @RequestMapping("ucreateorder")
    @ResponseBody
    public Map createOrder(int parkingId, String carNum){
        String result=userService.createOrder(parkingId,carNum);
        Map<String,Object> map=new HashMap<>();
        map.put("result",result);
        return map;
    }

    @RequestMapping("/uprice")
    @ResponseBody
    public Map getOrderPrice(String license, String openId){
        return userService.getPrice(license,openId);
    }

    @RequestMapping("/upay")
    @ResponseBody
    public String payOrder(String orderId,String msg){
        if(msg!=null){
            if(msg.equals("支付成功")) {
                userService.payOrderSuccess(orderId);
            }
        }
        return "666";
    }


}


