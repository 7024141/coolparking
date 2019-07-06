package com.example.coolparking.service;

import com.example.coolparking.dataobject.ParkingInfo;

public interface CreateParkingService {
    String insertParking(ParkingInfo parkingInfo);
    String createTable(String tablename, int carportnum);
}
