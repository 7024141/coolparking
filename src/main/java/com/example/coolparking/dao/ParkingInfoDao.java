package com.example.coolparking.dao;

import com.example.coolparking.dataobject.ParkingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingInfoDao extends JpaRepository<ParkingInfo,Integer> {
}
