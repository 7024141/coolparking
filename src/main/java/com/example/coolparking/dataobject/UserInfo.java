package com.example.coolparking.dataobject;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    private String openId;
    public UserInfo(){

    }
}
