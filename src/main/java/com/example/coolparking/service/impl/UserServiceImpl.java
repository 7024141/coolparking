package com.example.coolparking.service.impl;

import com.example.coolparking.dao.ParkingCarportDao;
import com.example.coolparking.dao.ParkingInfoDao;
import com.example.coolparking.dao.ParkingOrderDao;
import com.example.coolparking.dataobject.ParkingCarport;
import com.example.coolparking.dataobject.ParkingOrder;
import com.example.coolparking.service.UserService;
import com.example.coolparking.utils.OrderCreateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    ParkingOrderDao parkingOrderDao;
    @Autowired
    ParkingCarportDao parkingCarportDao;
    @Autowired
    ParkingInfoDao parkingInfoDao;
    @Override
    public List<ParkingOrder> findOrderById(String openId) {
        return parkingOrderDao.findOrderById(openId);
    }

    @Override
    public void createOrder(int parkingId,String carNum) {
        String tableName=parkingInfoDao.findById(parkingId).orElse(null).getCarportTable();
        ParkingCarport pc=parkingCarportDao.parkingFindFreeCarports(tableName).get(0);
        ParkingOrder p = OrderCreateUtil.orderCreate(parkingId, carNum, pc.getCarportNum());
        parkingOrderDao.createOrder(p.getOrderId(), p.getLicenseNum(), p.getParkingId(), p.getCarportNum());
        parkingCarportDao.parkingCarportUseEdit(tableName,pc.getCarportNum(),pc.isCarState());
    }
}
