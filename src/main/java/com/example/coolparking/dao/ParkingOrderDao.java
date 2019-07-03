package com.example.coolparking.dao;

import com.example.coolparking.dataobject.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingOrderDao extends JpaRepository<ParkingOrder,String> {
    @Query(value = "select * from parking_order where open_id=?1",nativeQuery = true)
    List<ParkingOrder> findOrderById(String openId);
}
