package com.example.coolparking.controller;

import com.example.coolparking.dataobject.ParkingCarport;
import com.example.coolparking.log.Log;
import com.example.coolparking.service.ParkingService;
import com.example.coolparking.service.WebSocket;
import com.example.coolparking.utils.CookieUtil;
import com.example.coolparking.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/pservice")
public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @Autowired
    WebSocket webSocket;

    @RequestMapping("/plogin")
    public String parkingToLogin(Model model){
        String UUID = UUIDUtil.createUUID();
        model.addAttribute("UUID", UUID);
        return "parkingLogin";
    }

    @RequestMapping("/pmain")
    public String test(Model model, HttpServletRequest request,
                       @RequestParam("parkingId")int parkingId,
                       @RequestParam(value = "page",defaultValue = "0")int page){
        Cookie cookie = CookieUtil.get(request, String.valueOf(parkingId));
        if(cookie != null){
            model.addAttribute("UUID", cookie.getValue());
        }
        model.addAttribute("parkingId", parkingId);
        model.addAttribute("currentPage",page);
        model.addAttribute("parkingPrice", parkingService.parkingGetPrice(parkingId).toString());
        return "parkingMain";
    }

    @RequestMapping("/pcarport")
    public String parkingMain(Model model, HttpServletRequest request,
                              @RequestParam("parkingId")int parkingId,
                              @RequestParam(value = "page",defaultValue = "0")int page,
                              @RequestParam(value = "size",defaultValue = "4")int size){
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<ParkingCarport> parkingCarportPage = parkingService.findAll(pageRequest,parkingId);
        Cookie cookie = CookieUtil.get(request, String.valueOf(parkingId));
        if(cookie != null){
            model.addAttribute("UUID", cookie.getValue());
        }
        model.addAttribute("parkingId", parkingId);
        model.addAttribute("parkingName", parkingService.parkingFindName(parkingId));
        model.addAttribute("parkingCarportPage",parkingCarportPage);
        model.addAttribute("currentPage",page);
        return "parkingCarport";
    }

    @PostMapping("/pedit")
    public void pedit( HttpServletRequest request,int parkingId,int page,String parkingCarportNum,boolean ableState) {
        Cookie cookie = CookieUtil.get(request, String.valueOf(parkingId));
        if (parkingService.parkingCarportEdit(parkingId, parkingCarportNum, ableState)) {
            System.out.println("更新成功");
            if(cookie != null){
                webSocket.sendInfo(cookie.getValue(),"更新成功");
            }
        } else {
            //更新失败
            System.out.println("更新失败");
            if(cookie != null){
                webSocket.sendInfo(cookie.getValue(),"更新失败");
            }
        }
    }

    @RequestMapping("/porder")
    public String parkingToOrder(HttpServletRequest request,Model model,int parkingId,int page,String type){
        System.out.println(type);
        if(type!=null){
            model.addAttribute("type",type);
            if(type.equals("recentOrder")){
                model.addAttribute("parkingOrders",parkingService.parkingFindRecentOrders(parkingId));
            }
            else if(type.equals("allOrder")){
                model.addAttribute("parkingOrders",parkingService.parkingFindAllOrders(parkingId));
            }
        }
        Cookie cookie = CookieUtil.get(request, String.valueOf(parkingId));
        if(cookie != null){
            model.addAttribute("UUID", cookie.getValue());
        }
        model.addAttribute("parkingName", parkingService.parkingFindName(parkingId));
        model.addAttribute("parkingId",parkingId);
        model.addAttribute("type",type);
        model.addAttribute("currentPage",page);
        return "parkingOrder";
    }


    @RequestMapping("/pmodifyprice")
    public void parkingModifyPrice(HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
        int len = request.getContentLength();
        byte[] callbackBody = new byte[len];
        try {
            ServletInputStream sis = request.getInputStream();
            sis.read(callbackBody, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //接收json数据
        JSONObject jsonObject = new JSONObject(new String(callbackBody));
        if(jsonObject.getLong("randomNum")>0){
            if(parkingService.parkingSetPrice(jsonObject.getInt("parkingId"), jsonObject.getString("parkingPrice"))){
                response.getWriter().print("true");
            }
            else{
                response.getWriter().print("false");
            }
        }
    }

    @RequestMapping("/pgetlog")
    public void pGetLog(HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
        int id;

        int len = request.getContentLength();
        byte[] callbackBody = new byte[len];
        try {
            ServletInputStream sis = request.getInputStream();
            sis.read(callbackBody, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //接收json数据
        JSONObject jsonObject = new JSONObject(new String(callbackBody));
        id = jsonObject.getInt("parkingId");

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = df.format(date);
        String path = ".\\txtlog\\"+id+"-"+dateTime+".txt";
        String log = Log.brFile(path);

        response.setCharacterEncoding("utf-8");
        response.getWriter().print(log);
    }
}