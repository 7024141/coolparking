package com.example.coolparking.dao;

import com.example.coolparking.dataobject.ParkingCarport;
import com.example.coolparking.utils.TableNameProviderUtil;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ParkingCarportDao {
    @Results(id="carportMap",value = {
            @Result(column = "carport_num",property = "carportNum",id=true),
            @Result(column = "car_state",property = "carState"),
            @Result(column = "able_state",property = "ableState")
    })
    @SelectProvider(type = TableNameProviderUtil.class,method = "selectCarportSQL")
    List<ParkingCarport> parkingFindAllCarports(String parkingCarportTableName);

    @UpdateProvider(type = TableNameProviderUtil.class,method = "updateCarportSQL")
    boolean parkingCarportEdit(String parkingCarportTableName,String parkingCarportNum,boolean ableState);
}
