package com.example.coolparking.service.impl;

import com.example.coolparking.dao.ParkingOrderDao;
import com.example.coolparking.dataobject.ParkingOrder;
import com.example.coolparking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    ParkingOrderDao parkingOrderDao;
    @Override
    public List<ParkingOrder> findOrderById(String openId) {
        return parkingOrderDao.findOrderById(openId);
    }
}
