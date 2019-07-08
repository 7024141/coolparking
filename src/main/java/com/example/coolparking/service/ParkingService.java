package com.example.coolparking.service;

import com.example.coolparking.dataobject.AdminInfo;
import com.example.coolparking.dataobject.ParkingCarport;
import com.example.coolparking.dataobject.ParkingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ParkingService {
    String parkingMain(AdminInfo adminInfo);

    List<ParkingCarport> parkingFindAllCarports(int parkingId);

    String parkingFindName(int parkingId);

    boolean parkingCarportEdit(int parkingId,String parkingCarportNum,boolean ableState);

    List<ParkingOrder> parkingFindAllOrders(int parkingId);

    //boolean parkingLoginState(int parkingId);

    BigDecimal parkingGetPrice(int parkingId);

    boolean parkingSetPrice(int parkingId,String parkingPrice);

    Page<ParkingCarport> findAll(Pageable pageable,int parkingId);

    List<ParkingOrder> parkingFindRecentOrders(int parkingId);
}
