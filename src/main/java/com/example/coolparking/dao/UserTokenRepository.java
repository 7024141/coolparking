package com.example.coolparking.dao;

import com.example.coolparking.dataobject.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTokenRepository extends JpaRepository<UserToken,String> {
    @Query(value = "select * from user_token where value=?1 and state=0",nativeQuery = true)
    List<UserToken> findByValueAndState(String Value);
}
