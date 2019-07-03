package com.example.coolparking.utils;

import org.apache.ibatis.jdbc.SQL;

public class TableNameProviderUtil {
    public String selectCarportSQL(String TableName){
        return new SQL().SELECT("*").FROM(TableName).toString();
    }

    public String updateCarportSQL(String parkingCarportTableName,String parkingCarportNum,boolean ableState){
        return new SQL().UPDATE(parkingCarportTableName).SET("able_state="+(ableState?0:1)).WHERE("carport_num='"+parkingCarportNum+"'").toString();
    }
}
