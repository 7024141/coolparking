package com.example.coolparking.service.impl;

import com.example.coolparking.dao.AdminInfoDao;
import com.example.coolparking.dataobject.AdminInfo;
import com.example.coolparking.service.AdminInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminInfoServiceImpl implements AdminInfoService {
    @Autowired
    AdminInfoDao adminInfoDao;

    @Override
    public AdminInfo findByOpenId(String openId) {
        return adminInfoDao.findByOpenId(openId);
    }
}
