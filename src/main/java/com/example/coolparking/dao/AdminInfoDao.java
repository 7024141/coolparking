package com.example.coolparking.dao;

import com.example.coolparking.dataobject.AdminInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminInfoDao extends JpaRepository<AdminInfo,Integer> {
}
