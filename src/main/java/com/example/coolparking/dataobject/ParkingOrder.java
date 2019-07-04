package com.example.coolparking.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "parking_order")
public class ParkingOrder {
    @Id
    @GeneratedValue
    private String orderId;
    private String licenseNum;
    private int parkingId;
    private String carportNum;
    private String openId;
    private String createTime;
    private String finishTime;
    private boolean orderState;
    public ParkingOrder(){

    }
}
