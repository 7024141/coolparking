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
    @GeneratedValue
    private int parkingId;
    private String password;
    private boolean loginState;
    public AdminInfo(){

    }
}
