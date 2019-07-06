package com.example.coolparking.utils;

import com.example.coolparking.dataobject.ParkingOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class OrderUtil {
    private static Random numGen = new Random();
    private static char[] numbers = ("0123456789").toCharArray();
    public static ParkingOrder orderCreate(int parkingId,String carNum,String carportNum){
        ParkingOrder parkingOrder=new ParkingOrder();
        parkingOrder.setOrderId(new SimpleDateFormat("yyyyMMdd").format(new Date())+randomNumStr(6));
        parkingOrder.setLicenseNum(carNum);
        parkingOrder.setParkingId(parkingId);
        parkingOrder.setCarportNum(carportNum);
        return parkingOrder;
    }
    public static String randomNumStr(int length) {
        if (length < 1)
        {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++)
        {
            randBuffer[i] = numbers[numGen.nextInt(9)];
        }
        return new String(randBuffer);
    }

    public static List<ParkingOrder> modifyTime(List<ParkingOrder> list){
        for(int i=0;i<list.size();i++){
            String s=list.get(i).getCreateTime();
            list.get(i).setCreateTime(s.substring(0,s.length()-2));
            s=list.get(i).getFinishTime();
            if(s!=null){
                list.get(i).setFinishTime(s.substring(0,s.length()-2));
            }
        }
        return list;
    }
}
