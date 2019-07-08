package com.example.coolparking.controller;

import com.example.coolparking.dataobject.ParkingInfo;
import com.example.coolparking.dataobject.SystemInfo;
import com.example.coolparking.service.CreateParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/sservice")
public class SysController {
    @Autowired
    private CreateParkingService createParkingService;

    @RequestMapping("/slogin")
    public String sysToLogin(){
        return "sysLogin";
    }
    @RequestMapping("/smain")
    public String sysMain(String password){
        if(password!=null){

            if(createParkingService.findSystemAdmin(password)){
                return "sysManage";
            }
            else {
                return "redirect:/sservice/slogin";
            }

        }
        else {
            return "sysManage";
        }


    }

    @RequestMapping("/screate")
    public void sysCreate(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {

        ParkingInfo parkingInfo=new ParkingInfo();
        parkingInfo.setParkingName(request.getParameter("parkingname"));
        parkingInfo.setCarportTable(request.getParameter("tablename"));
        if(createParkingService.insertParking(parkingInfo).equals("创建成功")&&createParkingService.createTable(request.getParameter("tablename"), Integer.valueOf(request.getParameter("carportnum"))).equals("创建成功")){
            response.sendRedirect("/coolparking/sservice/smain");
        }
    }
}
