package com.example.coolparking.controller;

import com.example.coolparking.VO.ResponseOb;
import com.example.coolparking.dataobject.ParkingCarport;
import com.example.coolparking.dataobject.ParkingOrder;
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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
@RequestMapping("/pservice")
public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @Autowired
    WebSocket webSocket;

    @RequestMapping("/plogin")
    public String parkingToLogin(Model model, HttpServletResponse response){
        String UUID = UUIDUtil.createUUID();
        model.addAttribute("UUID", UUID);
        ResponseOb.addResponse(UUID, response);
        return "parkingLogin";
    }

    @RequestMapping("/pmain")
    public String test(Model model, HttpServletRequest request,
                       @RequestParam("parkingId")int parkingId,
                       @RequestParam(value = "page",defaultValue = "0")int page,
                       @RequestParam(value = "size",defaultValue = "4")int size){
        Cookie cookie = CookieUtil.get(request, String.valueOf(parkingId));
        if(cookie != null){
            model.addAttribute("UUID", cookie.getValue());
        }
        model.addAttribute("size",size);
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
        model.addAttribute("size",size);
        return "parkingCarport";
    }

    @RequestMapping("/update")
    public String update(Model model,
                         @RequestParam("parkingId")int parkingId,
                         @RequestParam(value = "page",defaultValue = "0")int page,
                         @RequestParam(value = "size",defaultValue = "4")int size){
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<ParkingCarport> parkingCarportPage = parkingService.findAll(pageRequest,parkingId);
        model.addAttribute("parkingId", parkingId);
        model.addAttribute("parkingName", parkingService.parkingFindName(parkingId));
        model.addAttribute("parkingCarportPage",parkingCarportPage);
        model.addAttribute("currentPage",page);
        model.addAttribute("size",size);
        return "parkingCarport::carportInfo";
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

    @RequestMapping("/preport")
    public String pReport(Model model,@RequestParam("parkingId")int parkingId){
        //获取今天的日期
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = df.format(date);
        //获取明天的日期
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,1);
        String nextTime= df.format(calendar.getTime());
        //获取昨日的日期
        calendar.add(calendar.DATE,-2);
        String time1= df.format(calendar.getTime());
        //
        calendar.add(calendar.DATE,-1);
        String time2= df.format(calendar.getTime());
        //
        calendar.add(calendar.DATE,-1);
        String time3= df.format(calendar.getTime());
        //
        calendar.add(calendar.DATE,-1);
        String time4= df.format(calendar.getTime());

        //计算今日总的营业额和出库车辆
        int totalToday = getDayPrice(parkingId,dateTime,nextTime);
        int todayCar = getDayNum(parkingId,dateTime,nextTime);
        //昨日
        int totalYe = getDayPrice(parkingId,time1,dateTime);
        int YeCar = getDayNum(parkingId,time1,dateTime);
        //
        int total2 = getDayPrice(parkingId,time2,time1);
        //
        int total3 = getDayPrice(parkingId,time3,time2);
        //
        int total4 = getDayPrice(parkingId,time4,time3);

        model.addAttribute("parkingId",parkingId);
        model.addAttribute("parkingName", parkingService.parkingFindName(parkingId));
        model.addAttribute("parkingPrice", parkingService.parkingGetPrice(parkingId).toString());
        //今天
        model.addAttribute("totalPrice",totalToday);
        model.addAttribute("totalCarNum",todayCar);
        model.addAttribute("time",dateTime);
        //昨天
        model.addAttribute("totalYePrice",totalYe);
        model.addAttribute("YeCarNum",YeCar);
        model.addAttribute("time1",time1);
        //
        model.addAttribute("time2",time2);
        model.addAttribute("time3",time3);
        model.addAttribute("time4",time4);
        model.addAttribute("price2",total2);
        model.addAttribute("price3",total3);
        model.addAttribute("price4",total4);

        return "parkingReport";
    }

    public int getDayNum(int parkingId, String date1, String date2){
        List<ParkingOrder> l = parkingService.findAllOder(parkingId,1, date1, date2); //获取所有账单
        return l.size();
    }

    public int getDayPrice(int parkingId, String date1, String date2){
        List<ParkingOrder> l = parkingService.findAllOder(parkingId,1, date1, date2); //获取所有账单
        float price = parkingService.parkingGetPrice(parkingId).floatValue(); //获取单价
        int length = l.size(); //订单数目
        int total = 0; //总营业额
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<length;i++){
            ParkingOrder o = l.get(i);
            String start = o.getCreateTime();
            String end = o.getFinishTime();
            Date d1 = null;
            try {
                d1 = df.parse(start);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date d2 = null;
            try {
                d2 = df.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = d2.getTime() - d1.getTime();
            long minute = diff/60/1000;
            int money = (int)(minute * price);
            System.out.println(money);
            total = total + money;
        }
        return total;
    }
}