package com.example.coolparking.service.impl;

import com.example.coolparking.dao.ParkingCarportDao;
import com.example.coolparking.dao.ParkingInfoDao;
import com.example.coolparking.dao.ParkingOrderDao;
import com.example.coolparking.dataobject.ParkingCarport;
import com.example.coolparking.dataobject.ParkingOrder;
import com.example.coolparking.log.Log;
import com.example.coolparking.service.UserService;
import com.example.coolparking.utils.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    ParkingOrderDao parkingOrderDao;
    @Autowired
    ParkingCarportDao parkingCarportDao;
    @Autowired
    ParkingInfoDao parkingInfoDao;
    @Override
    public List<ParkingOrder> findOrderById(String openId) {
        List<ParkingOrder> list = parkingOrderDao.findOrderById(openId);
        list =OrderUtil.modifyTime(list);
        return list;
    }

    @Override
    public void createOrder(int parkingId,String carNum) {
        String tableName=parkingInfoDao.findById(parkingId).orElse(null).getCarportTable();
        ParkingCarport pc=parkingCarportDao.parkingFindFreeCarports(tableName).get(0);
        ParkingOrder p = OrderUtil.orderCreate(parkingId, carNum, pc.getCarportNum());
        parkingOrderDao.createOrder(p.getOrderId(), p.getLicenseNum(), p.getParkingId(), p.getCarportNum());
        parkingCarportDao.parkingCarportUseEdit(tableName,pc.getCarportNum(),pc.isCarState());

        //写入内容
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = df.format(date);
        String content = "<p>"+dateTime + "    小车（"+carNum+ "）进入"+"停车位（"+p.getCarportNum()+"）"+"</p>\r\n";
        //写入文件路径
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime2 = df2.format(date);
        String path = ".\\txtlog\\"+p.getParkingId()+"-"+dateTime2+".txt";
        Log.bwFile( path, content,true);
    }

    @Override
    public Map getPrice(String license, String id){
        Date date = new Date();
        ParkingOrder o = parkingOrderDao.findLeaveOrder(license,0).get(0);
        BigDecimal price = parkingInfoDao.findById(o.getParkingId()).orElse(null).getParkingPrice();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = df.format(date);
        o.setFinishTime(dateTime);
        o.setOpenId(id);
        System.out.println(dateTime);
        parkingOrderDao.save(o);
        String strTime = o.getCreateTime();
        Date date1 = null;
        try {
            date1 = df.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = date.getTime() - date1.getTime();
        long minute = diff/60/1000;
        Map<String,Object> map = new HashMap<>();
        map.put("order",o);
        System.out.println(price);
        System.out.println(price.floatValue());
        map.put("price",(int)(minute * price.floatValue()));
        return map;
    }

    @Override
    public void payOrderSuccess(String orderId){
        ParkingOrder parkingOrder=parkingOrderDao.findById(orderId).orElse(null);
        int parkingId=parkingOrder.getParkingId();
        if(parkingOrder!=null){
            parkingOrder.setOrderState(true);
            parkingOrderDao.save(parkingOrder);
            parkingCarportDao.parkingCarportUseEdit(parkingInfoDao.findById(parkingId).orElse(null).getCarportTable(),parkingOrder.getCarportNum(),true);

            //写入内容
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = df.format(date);
            String content = "<p>"+dateTime + "    小车（"+parkingOrder.getLicenseNum()+ "）离开停车位（"+parkingOrder.getCarportNum()+"）"+"</p>\r\n";
            //写入文件路径
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime2 = df2.format(date);
            String path = ".\\txtlog\\"+parkingId+"-"+dateTime2+".txt";
            Log.bwFile( path, content,true);
        }
    }
}