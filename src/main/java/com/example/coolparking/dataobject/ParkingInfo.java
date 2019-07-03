package com.example.coolparking.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "parking_info")
public class ParkingInfo {
    @Id
    @GeneratedValue
    private int parkingId;
    private String parkingName;
    private String carportTable;
    public ParkingInfo(){

    }
}
