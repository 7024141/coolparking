package com.example.coolparking.service;

import com.example.coolparking.dataobject.AdminInfo;
import com.example.coolparking.dataobject.ParkingInfo;
import com.example.coolparking.dataobject.SystemInfo;

import java.util.List;

public interface SystemService {
    String insertParking(ParkingInfo parkingInfo);
    String createTable(String tablename, int carportnum);
    boolean findSystemAdmin(String password);
    List<AdminInfo> findAllAdmin();
    boolean editSystemAdmin(AdminInfo adminInfo);
    boolean deleteSystemAdmin(Integer parkingId, String password);
}
