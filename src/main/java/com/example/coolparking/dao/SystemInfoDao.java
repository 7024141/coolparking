package com.example.coolparking.dao;

import com.example.coolparking.dataobject.SystemInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SystemInfoDao extends JpaRepository<SystemInfo,Integer> {
    @Query(value = "select 1 from system_info where password = ? limit 1", nativeQuery = true)
    Integer findSystemInfoBy(String password);
}
