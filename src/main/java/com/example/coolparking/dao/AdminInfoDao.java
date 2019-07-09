package com.example.coolparking.dao;

import com.example.coolparking.dataobject.AdminInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AdminInfoDao extends JpaRepository<AdminInfo,Integer> {
    AdminInfo findByOpenId(String openId);
    @Modifying
    @Transactional
    @Query(value = "delete from admin_info where parking_id=?1 and password=?2", nativeQuery = true)
    void deleteAdmin(Integer parkingId, String password);
}
