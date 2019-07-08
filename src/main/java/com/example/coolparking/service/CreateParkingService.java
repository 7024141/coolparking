package com.example.coolparking.service;

import com.example.coolparking.dataobject.ParkingInfo;
import com.example.coolparking.dataobject.SystemInfo;

public interface CreateParkingService {
    String insertParking(ParkingInfo parkingInfo);
    String createTable(String tablename, int carportnum);
    boolean findSystemAdmin(String password);
}
