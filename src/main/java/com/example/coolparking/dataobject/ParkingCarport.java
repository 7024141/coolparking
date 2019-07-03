package com.example.coolparking.dataobject;

import lombok.Data;

@Data
public class ParkingCarport {
    private String carportNum;
    private boolean carState;
    private boolean ableState;
    public ParkingCarport(){

    }
}
