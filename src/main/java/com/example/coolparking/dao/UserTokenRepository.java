package com.example.coolparking.dao;

import com.example.coolparking.dataobject.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken,String> {

}
