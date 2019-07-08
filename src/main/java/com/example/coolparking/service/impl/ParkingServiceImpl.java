package com.example.coolparking.service.impl;

import com.example.coolparking.dao.AdminInfoDao;
import com.example.coolparking.dao.ParkingCarportDao;
import com.example.coolparking.dao.ParkingInfoDao;
import com.example.coolparking.dao.ParkingOrderDao;
import com.example.coolparking.dataobject.AdminInfo;
import com.example.coolparking.dataobject.ParkingCarport;
import com.example.coolparking.dataobject.ParkingInfo;
import com.example.coolparking.dataobject.ParkingOrder;
import com.example.coolparking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    ParkingOrderDao parkingOrderDao;

    @Autowired
    AdminInfoDao adminInfoDao;

    @Autowired
    ParkingInfoDao parkingInfoDao;

    @Autowired
    ParkingCarportDao parkingCarportDao;

    @Override
    public String parkingMain(AdminInfo adminInfo) {
        AdminInfo ai=adminInfoDao.findById(adminInfo.getParkingId()).orElse(null);
        if(ai!=null){
            if(ai.getPassword().equals(adminInfo.getPassword())){
                System.out.println("登录成功");
                ai.setLoginState(true);
                adminInfoDao.save(ai);
                return "登录成功";
            }
            else {
                System.out.println("密码错误");
                return "密码错误";
            }
        }
        else {
            System.out.println("用户不存在");
            return  "用户不存在";
        }
    }

    @Override
    public List<ParkingCarport> parkingFindAllCarports(int parkingId) {
        return parkingCarportDao.parkingFindAllCarports(parkingInfoDao.findById(parkingId).orElse(null).getCarportTable());
    }

    @Override
    public String parkingFindName(int parkingId) {
        return parkingInfoDao.findById(parkingId).orElse(null).getParkingName();
    }

    @Override
    public boolean parkingCarportEdit(int parkingId, String parkingCarportNum, boolean ableState) {
        return parkingCarportDao.parkingCarportEdit(parkingInfoDao.findById(parkingId).orElse(null).getCarportTable(),parkingCarportNum,ableState);
    }

    @Override
    public List<ParkingOrder> parkingFindAllOrders(int parkingId) {
        return parkingOrderDao.findAll();
    }

    @Override
    public boolean parkingLoginState(int parkingId){
        return adminInfoDao.findById(parkingId).orElse(null).isLoginState();
    }

    @Override
    public BigDecimal parkingGetPrice(int parkingId){
        return parkingInfoDao.findById(parkingId).orElse(null).getParkingPrice();
    }

    @Override
    public boolean parkingSetPrice(int parkingId,String parkingPrice){
        try{
            BigDecimal bigDecimal=new BigDecimal(parkingPrice);
            ParkingInfo parkingInfo = parkingInfoDao.findById(parkingId).orElse(null);
            parkingInfo.setParkingPrice(bigDecimal);
            parkingInfoDao.save(parkingInfo);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }


    @Override
    public Page<ParkingCarport> findAll(Pageable pageable,int parkingId){
        List<ParkingCarport> parkingCarportList=parkingCarportDao.parkingFindAllCarports(parkingInfoDao.findById(parkingId).orElse(null).getCarportTable());
        int start=(int)pageable.getOffset();
        int end=(start+pageable.getPageSize())>parkingCarportList.size()?parkingCarportList.size():(start+pageable.getPageSize());
        Page<ParkingCarport> parkingCarportPage=new PageImpl<ParkingCarport>(parkingCarportList.subList(start,end),pageable,parkingCarportList.size());
        return parkingCarportPage;
    }
}
