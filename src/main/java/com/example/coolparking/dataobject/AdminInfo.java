package com.example.coolparking.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "admin_info")
public class AdminInfo {
    @Id
    private int parkingId;
    private String password;
    private String openId;
    public AdminInfo(){

    }

    public AdminInfo(int parkingId, String password){
        this.parkingId = parkingId;
        this.password = password;
    }
}
