package com.example.coolparking.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class UserToken {
    @Id
    private String uuid;

    private String value;

    private Date loginTime;

    private boolean state;

    public UserToken(String uuid, String value)
    {
        this.uuid = uuid;
        this.value = value;
    }

    public UserToken(){}
}
