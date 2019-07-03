package com.example.coolparking.service;

import com.example.coolparking.dataobject.ParkingCarport;
import com.example.coolparking.dataobject.ParkingOrder;

import java.util.List;

public interface ParkingService {
    String parkingMain(int parkingId,String password);

    List<ParkingCarport> parkingFindAllCarports(int parkingId);

    String parkingFindName(int parkingId);

    boolean parkingCarportEdit(int parkingId,String parkingCarportNum,boolean ableState);

    List<ParkingOrder> parkingFindAllOrders(int parkingId);

    boolean parkingLoginState(int parkingId);
}
