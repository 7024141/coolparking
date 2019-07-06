package com.example.coolparking.controller;

import com.example.coolparking.dataobject.AdminInfo;
import com.example.coolparking.dataobject.ParkingCarport;
import com.example.coolparking.log.Log;
import com.example.coolparking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
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

    @RequestMapping("/plogin")
    public String parkingToLogin(){
        return "parkingLogin";
    }

    @RequestMapping("/pmain")
    public String parkingMain(Model model,AdminInfo adminInfo,@RequestParam(value = "page",defaultValue = "0")int page,@RequestParam(value = "size",defaultValue = "4")int size){
        if(parkingService.parkingLoginState(adminInfo.getParkingId())&&adminInfo.getPassword()==null){
            PageRequest pageRequest=PageRequest.of(page,size);
            Page<ParkingCarport> parkingCarportPage = parkingService.findAll(pageRequest,adminInfo.getParkingId());
            model.addAttribute("parkingId", adminInfo.getParkingId());
            model.addAttribute("parkingName", parkingService.parkingFindName(adminInfo.getParkingId()));
            model.addAttribute("parkingPrice", parkingService.parkingGetPrice(adminInfo.getParkingId()).toString());
            model.addAttribute("parkingCarportPage",parkingCarportPage);
            model.addAttribute("currentPage",page);
            return "parkingMain";
        }
        else {
//            if(parkingService.parkingLoginState(adminInfo.getParkingId())){
//                //禁止登录
//                System.out.println("禁止登录");
//                return "parkingLogin";
//            }
            String str = parkingService.parkingMain(adminInfo.getParkingId(),adminInfo.getPassword());
            if(str.equals("密码错误")){
                //弹窗
                return "redirect:/pservice/plogin";
            }
            else if(str.equals("用户不存在")){
                //弹窗
                return "redirect:/pservice/plogin";
            }
            else if(str.equals("登录成功")){
                PageRequest pageRequest=PageRequest.of(page,size);
                Page<ParkingCarport> parkingCarportPage = parkingService.findAll(pageRequest,adminInfo.getParkingId());
                model.addAttribute("parkingId", adminInfo.getParkingId());
                model.addAttribute("parkingPrice", parkingService.parkingGetPrice(adminInfo.getParkingId()).toString());
                model.addAttribute("parkingName", parkingService.parkingFindName(adminInfo.getParkingId()));
                model.addAttribute("currentPage",page);
                model.addAttribute("parkingCarportPage",parkingCarportPage);
                return "parkingMain";
            }
            return "redirect:/pservice/plogin";
        }
    }

    @RequestMapping("/pedit")
    public String parkingEdit(int parkingId,int page,String parkingCarportNum,boolean ableState) {
        if (parkingService.parkingCarportEdit(parkingId, parkingCarportNum, ableState)) {
            System.out.println("更新成功");
        } else {
            //更新失败
            System.out.println("更新失败");
        }
        return "redirect:/pservice/pmain?parkingId=" + parkingId+"&page="+page;
    }

    @RequestMapping("/porder")
    public String parkingToOrder(Model model,int parkingId,int page,String type){
        if(type!=null){
            model.addAttribute("type",type);
            if(type.equals("recentOrder")){
                model.addAttribute("parkingOrders",parkingService.parkingFindRecentOrders(parkingId));
            }
            else if(type.equals("allOrder")){
                model.addAttribute("parkingOrders",parkingService.parkingFindAllOrders(parkingId));
            }
        }
        model.addAttribute("parkingName", parkingService.parkingFindName(parkingId));
        model.addAttribute("parkingPrice", parkingService.parkingGetPrice(parkingId).toString());
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