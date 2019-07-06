package com.example.coolparking.dataobject;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "parking_info")
public class ParkingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int parkingId;
    private String parkingName;
    private String carportTable;
    @Column(name = "parkingPrice",columnDefinition="3.0",nullable=false)
    private BigDecimal parkingPrice = new BigDecimal(3.0);
    public ParkingInfo(){

    }
}
