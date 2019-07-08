package com.example.coolparking.service.impl;

import com.example.coolparking.dao.UserTokenRepository;
import com.example.coolparking.dataobject.UserToken;
import com.example.coolparking.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    UserTokenRepository repository;

    @Override
    public UserToken findOne(String UUID) {
        return repository.findById(UUID).orElse(null);
    }

    @Override
    public UserToken saveOne(UserToken userToken) {
        return repository.save(userToken);
    }
}
