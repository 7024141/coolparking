package com.example.coolparking.service.impl;

import com.example.coolparking.dao.ParkingCarportDao;
import com.example.coolparking.dao.ParkingInfoDao;
import com.example.coolparking.dataobject.ParkingInfo;
import com.example.coolparking.service.CreateParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateParkingServiceImpl implements CreateParkingService {
    @Autowired
    ParkingCarportDao parkingCarportDao;

    @Autowired
    ParkingInfoDao parkingInfoDao;


    @Override
    public String insertParking(ParkingInfo parkingInfo){
        try{
            parkingInfoDao.save(parkingInfo);
        }
        catch (Exception e){
            return "创建失败";
        }
        return "创建成功";
    }

    @Override
    public String createTable(String tablename, int carportnum){
        try{
            parkingCarportDao.createParking(tablename);
        }
        catch (Exception e){
            return "创建失败";
        }
        parkingCarportDao.insertTable(tablename, carportnum);
        return "创建成功";
    }
}
