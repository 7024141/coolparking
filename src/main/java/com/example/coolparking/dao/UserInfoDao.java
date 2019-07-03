package com.example.coolparking.dao;

import com.example.coolparking.dataobject.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoDao extends JpaRepository<UserInfo,Integer> {
}
