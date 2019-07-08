package com.example.coolparking.service;

import com.example.coolparking.dataobject.UserToken;

public interface UserTokenService {
    UserToken findOne(String UUID);
    UserToken saveOne(UserToken userToken);
}
