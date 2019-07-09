package com.example.coolparking.controller;

import com.example.coolparking.dataobject.AdminInfo;
import com.example.coolparking.dataobject.ParkingInfo;
import com.example.coolparking.service.SystemService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/sservice")
public class SysController {
    @Autowired
    private SystemService systemService;

    @RequestMapping("/slogin")
    public String sysToLogin(){
        return "sysLogin";
    }
    @RequestMapping("/smain")
    public String sysMain(Model model, String password){
        if(password!=null){

            if(systemService.findSystemAdmin(password)){
                model.addAttribute("adminInfos", systemService.findAllAdmin());
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
        if(systemService.insertParking(parkingInfo).equals("创建成功")&& systemService.createTable(request.getParameter("tablename"), Integer.valueOf(request.getParameter("carportnum"))).equals("创建成功")){
            response.sendRedirect("/coolparking/sservice/smain");
        }
    }

    @RequestMapping("/screateadmin")
    public String createAdmin(Model model, Integer newAdminId, String newAdminPassword){
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setParkingId(newAdminId);
        adminInfo.setPassword(newAdminPassword);
        if(systemService.editSystemAdmin(adminInfo)){
            model.addAttribute("adminInfos", systemService.findAllAdmin());
            return "sysManage";
        }
        model.addAttribute("adminInfos", systemService.findAllAdmin());
        return "sysManage";
    }

    @RequestMapping("/sedit")
    public void sysEdit(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {

        int length = request.getContentLength();
        byte[] callbackBody = new byte[length];
        try {
            ServletInputStream sis = request.getInputStream();
            sis.read(callbackBody, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //接收json数据
        JSONObject jsonObject = new JSONObject(new String(callbackBody));
        if(jsonObject.getLong("randomNum")>0){
            AdminInfo adminInfo = new AdminInfo();
            adminInfo.setParkingId(jsonObject.getInt("parkingId"));
            adminInfo.setPassword(jsonObject.getString("password"));
            if(systemService.editSystemAdmin(adminInfo)){
                response.getWriter().print("true");
            }
            else {
                response.getWriter().print("false");
            }
        }
    }
    @RequestMapping("/sdelete")
    public String sysDelete(Model model, Integer parkingId, String password){
        systemService.deleteSystemAdmin(parkingId, password);
        model.addAttribute("adminInfos", systemService.findAllAdmin());
        return "sysManage";
    }
}
