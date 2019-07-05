package com.example.coolparking.service;


import com.example.coolparking.dataobject.ParkingOrder;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<ParkingOrder> findOrderById(String openId);
    void createOrder(int parkingId,String carNum);
    Map getPrice(String license, String id);
    void payOrderSuccess(String orderId);
}
