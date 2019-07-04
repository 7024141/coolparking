package com.example.coolparking.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "parking_info")
public class ParkingInfo {
    @Id
    @GeneratedValue
    private int parkingId;
    private String parkingName;
    private String carportTable;
    private BigDecimal parkingPrice;
    public ParkingInfo(){

    }
}
