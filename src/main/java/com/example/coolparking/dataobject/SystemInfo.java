package com.example.coolparking.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "system_info")
public class SystemInfo {
    @Id
    String password;
    public SystemInfo(){

    }
}
