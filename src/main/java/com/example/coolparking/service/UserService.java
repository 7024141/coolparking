package com.example.coolparking.service;


import com.example.coolparking.dataobject.ParkingOrder;

import java.util.List;

public interface UserService {
    List<ParkingOrder> findOrderById(String openId);
    void createOrder(int parkingId,String carNum);
}
