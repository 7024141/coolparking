package com.example.coolparking.utils;

import org.apache.ibatis.jdbc.SQL;

public class TableNameProviderUtil {
    public String selectCarportSQL(String TableName){
        return new SQL().SELECT("*").FROM(TableName).toString();
    }

    public String updateCarportAbleSQL(String parkingCarportTableName,String parkingCarportNum,boolean ableState){
        return new SQL().UPDATE(parkingCarportTableName).SET("able_state="+(ableState?0:1)).WHERE("carport_num='"+parkingCarportNum+"'").toString();
    }

    public String selectFreeCarportSQL(String TableName){
        return new SQL().SELECT("*").FROM(TableName).WHERE("able_state=1").AND().WHERE("car_state=0").toString();
    }

    public String updateCarportUseSQL(String parkingCarportTableName,String parkingCarportNum,boolean carState){
        return new SQL().UPDATE(parkingCarportTableName).SET("car_state="+(carState?0:1)).WHERE("carport_num='"+parkingCarportNum+"'").toString();
    }

    public String createParkingSQL(String tablename){
        String sql = "create table "+tablename+"(carport_num varchar(8) primary key,car_state tinyint(1),able_state tinyint(1))";
        return sql;
    }

    public String insertParkingSQL(String tablename, int carportnum){
        String sql = "insert into " + tablename + " values";
        for(int i=1;i<carportnum;i++){
            sql += "(concat('T'," + i + "),0,1),";
        }
        sql += "(concat('T'," + carportnum + "),0,1)";

        return sql;
    }
}
