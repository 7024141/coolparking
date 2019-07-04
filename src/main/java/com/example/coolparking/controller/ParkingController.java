package com.example.coolparking.controller;

import com.example.coolparking.dataobject.AdminInfo;
import com.example.coolparking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String parkingMain(Model model,AdminInfo adminInfo){
        if(parkingService.parkingLoginState(adminInfo.getParkingId())&&adminInfo.getPassword()==null){
            model.addAttribute("parkingCarports", parkingService.parkingFindAllCarports(adminInfo.getParkingId()));
            model.addAttribute("parkingId", adminInfo.getParkingId());
            model.addAttribute("parkingName", parkingService.parkingFindName(adminInfo.getParkingId()));
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
                //
                model.addAttribute("parkingCarports", parkingService.parkingFindAllCarports(adminInfo.getParkingId()));
                model.addAttribute("parkingId", adminInfo.getParkingId());
                model.addAttribute("parkingName", parkingService.parkingFindName(adminInfo.getParkingId()));
                return "parkingMain";
            }
            return "redirect:/pservice/plogin";
        }
    }

    @RequestMapping("/pedit")
    public String parkingEdit(int parkingId,String parkingCarportNum,boolean ableState) {
        if (parkingService.parkingCarportEdit(parkingId, parkingCarportNum, ableState)) {
            System.out.println("更新成功");
        } else {
            //更新失败
            System.out.println("更新失败");
        }
        return "redirect:/pservice/pmain?parkingId=" + parkingId;
    }

    @RequestMapping("/porder")
    public String parkingToOrder(Model model,int parkingId){
        model.addAttribute("parkingName", parkingService.parkingFindName(parkingId));
        model.addAttribute("parkingOrders",parkingService.parkingFindAllOrders(parkingId));
        model.addAttribute("parkingId",parkingId);
        return "parkingOrder";
    }
}