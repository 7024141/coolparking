package com.example.coolparking.service;

import com.example.coolparking.dataobject.AdminInfo;

public interface AdminInfoService {
    AdminInfo findByOpenId(String openId);
}
