package com.example.coolparking.controller;

import com.example.coolparking.VO.AdminInfoTrans;
import com.example.coolparking.VO.ResultVO;
import com.example.coolparking.aspect.AuthorizeAspect;
import com.example.coolparking.dataobject.AdminInfo;
import com.example.coolparking.dataobject.UserToken;
import com.example.coolparking.service.AdminInfoService;
import com.example.coolparking.service.ParkingService;
import com.example.coolparking.service.UserTokenService;
import com.example.coolparking.service.WebSocket;
import com.example.coolparking.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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

    @Autowired
    AdminInfoService adminInfoService;

    @PostMapping("/login")
    public void login(AdminInfoTrans adminInfoTrans){
        AdminInfo adminInfo = new AdminInfo(adminInfoTrans.getParkingId(), adminInfoTrans.getPassword());
        String result = parkingService.parkingMain(adminInfo);
        webSocket.sendInfo(adminInfoTrans.getUUID(), result);
        if(result.equals("登录成功")){
            AuthorizeAspect.token = String.valueOf(adminInfoTrans.getParkingId());
            AuthorizeAspect.UUID = adminInfoTrans.getUUID();
            //2、创建UUID与value
            UserToken userToken = new UserToken(adminInfoTrans.getUUID(), String.valueOf( adminInfoTrans.getParkingId()));
            userToken.setLoginTime(new Date());
            userTokenService.saveOne(userToken);

            //3、将UUID插入cookie
            //CookieUtil.set(response, String.valueOf(adminInfoTrans.getParkingId()), adminInfoTrans.getUUID(), 600);
        }
    }

    @PostMapping("/setcookie")
    public ResultVO setcookie(HttpServletResponse response,
                              @RequestParam("UUID")String UUID){
        UserToken userToken = userTokenService.findOne(UUID);
        if(userToken != null){
            CookieUtil.set(response, userToken.getValue(), UUID, 600);
            ResultVO resultVO = new ResultVO(0, userToken.getValue());
            return resultVO;
        }
        else return null;
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

    @GetMapping("/qrlogin")
    public void qrlogin(@RequestParam("openId")String openId, @RequestParam("UUID")String UUID){
        if(!StringUtils.isEmpty(UUID)){
            //1、AdminInfo中查询openId
            AdminInfo adminInfo = adminInfoService.findByOpenId(openId);
            if(adminInfo != null){
                //2、存在，插入UserToken，value为openId
                UserToken userToken = new UserToken(UUID, String.valueOf(adminInfo.getParkingId()));
                userToken.setLoginTime(new Date());
                userTokenService.saveOne(userToken);

                //3、发送消息
                webSocket.sendInfo(UUID, "登录成功");
            }else{
                webSocket.sendInfo(UUID, "没有登录权限");
            }
        }
    }
}
