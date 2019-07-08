package com.example.coolparking.controller;

import com.example.coolparking.VO.AdminInfoTrans;
import com.example.coolparking.dataobject.AdminInfo;
import com.example.coolparking.dataobject.UserToken;
import com.example.coolparking.service.ParkingService;
import com.example.coolparking.service.UserTokenService;
import com.example.coolparking.service.WebSocket;
import com.example.coolparking.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RequestMapping("/check")
@Controller
public class LoginCheckController {

    @Autowired
    ParkingService parkingService;

    @Autowired
    WebSocket webSocket;

    @Autowired
    UserTokenService userTokenService;

    @PostMapping("/login")
    public void login(AdminInfoTrans adminInfoTrans, HttpServletResponse response){
        AdminInfo adminInfo = new AdminInfo(adminInfoTrans.getParkingId(), adminInfoTrans.getPassword());
        String result = parkingService.parkingMain(adminInfo);
        webSocket.sendInfo(adminInfoTrans.getUUID(), result);
        if(result.equals("登录成功")){
            //2、创建UUID与value
            UserToken userToken = new UserToken(adminInfoTrans.getUUID(), String.valueOf( adminInfoTrans.getParkingId()));
            userToken.setLoginTime(new Date());
            userTokenService.saveOne(userToken);

            //3、将UUID插入cookie
            CookieUtil.set(response, String.valueOf(adminInfoTrans.getParkingId()), adminInfoTrans.getUUID(), 600);
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response,
                         @RequestParam("parkingId")int parkingId){
        //1、查询cookie
        Cookie cookie = CookieUtil.get(request, String.valueOf(parkingId));
        if(cookie != null){
            //2、删除usertoken
            UserToken userToken = userTokenService.findOne(cookie.getValue());
            userToken.setState(true);
            userTokenService.saveOne(userToken);
            //3、删除cookie
            CookieUtil.set(response, String.valueOf(parkingId), null, 0);
        }
        return "redirect:/pservice/plogin";
    }
}
