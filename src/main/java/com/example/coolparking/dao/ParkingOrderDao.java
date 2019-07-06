package com.example.coolparking.dao;

import com.example.coolparking.dataobject.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParkingOrderDao extends JpaRepository<ParkingOrder,String> {
    @Query(value = "select * from parking_order where open_id=?1", nativeQuery = true)
    List<ParkingOrder> findOrderById(String openId);

    @Modifying
    @Transactional
    @Query(value = "insert into parking_order(order_id,license_num,parking_id,carport_num) values(?1,?2,?3,?4)", nativeQuery = true)
    void createOrder(String orderId, String licenseNum, int parkingId, String carportNum);

    @Query(value = "select * from parking_order where license_num=?1 AND order_state=?2",nativeQuery = true)
    List<ParkingOrder> findLeaveOrder(String license,int state);

    @Query(value = "select * from parking_order where DATEDIFF(create_time,?2)>=0 AND parking_id = ?1",nativeQuery = true)
    List<ParkingOrder> findRecentOrder(int parkingId,String currentTime);

}

