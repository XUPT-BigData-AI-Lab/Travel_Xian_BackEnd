package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.OrderInfoDtoDao;
import com.coach_station.bootmall.dto.OrderInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Auther: yjw
 * @Date: 2022/01/12/13:47
 * @Description:
 */
@Service
public class OrderDtoService {

    @Autowired
    OrderInfoDtoDao orderInfoDao;

    public Map<String,Object> findMasterOrdersByTime(Long userId, Integer orderType, Long startTime, Long endTime, Integer page, Integer size){
        List<OrderInfoDto> orderInfos = orderInfoDao.findAllMasterOrdersByTime(userId, orderType, startTime, endTime);
        if (orderInfos == null || orderInfos.size() == 0){
            return null;
            //todo 打日志
        }
        return packPageOrders(page,size,orderInfos);
    }

    public Map<String,Object> findRefundMasterOrdersByTime(Long userId, Integer orderType, Long startTime, Long endTime, Integer page, Integer size, Integer refundAll, Integer refundPart){
        List<OrderInfoDto> orderInfos = orderInfoDao.findRefundMasterOrdersByTime(userId, orderType, refundAll, refundPart, startTime, endTime);
        if (orderInfos == null || orderInfos.size() == 0){
            return null;
            //todo 打日志
        }
        return packPageOrders(page,size,orderInfos);
    }

    public Map<String,Object> findMasterOrdersByTimeAndOrderStatus(Long userId, Integer orderType, Long startTime, Long endTime, Integer page, Integer size, Integer orderStatus){
        System.out.println("orderstatus:" + orderStatus);
        List<OrderInfoDto> orderInfos = orderInfoDao.findMasterOrdersByTimeAndOrderStatus(userId, orderType, orderStatus, startTime, endTime);
        if (orderInfos == null || orderInfos.size() == 0){
            return null;
            //todo 打日志
        }
        return packPageOrders(page,size,orderInfos);
    }

    public Map<String,Object> findAllMasterOrders(Long userId, Integer orderType, Integer page, Integer size){
        List<OrderInfoDto> orderInfos = orderInfoDao.findAllMasterOrders(userId, orderType);
        if (orderInfos == null || orderInfos.size() == 0){
            return null;
            //todo 打日志
        }
        return packPageOrders(page,size,orderInfos);
    }

    public Map<String,Object> findRefundOrders(Long userId, Integer orderType, Integer page, Integer size, Integer refundAll, Integer refundPart){
        List<OrderInfoDto> orderInfos = orderInfoDao.findRefundOrders(userId, orderType, refundAll, refundPart);
        if (orderInfos == null || orderInfos.size() == 0){
            return null;
            //todo 打日志
        }
        return packPageOrders(page,size,orderInfos);
    }

    public Map<String,Object> findMasterOrdersByOrderStatus(Long userId, Integer orderType, Integer orderStatus,Integer page, Integer size){
        List<OrderInfoDto> orderInfos = orderInfoDao.findMasterOrdersByOrderStatus(userId, orderType, orderStatus);
        if (orderInfos == null || orderInfos.size() == 0){
            return null;
            //todo 打日志
        }
        return packPageOrders(page,size,orderInfos);
    }

    public List<OrderInfoDto> findOrdersByOrderStatus(Long userId, Integer orderType, Integer orderStatus){
        return orderInfoDao.findMasterOrdersByOrderStatus(userId, orderType, orderStatus);
    }

    public Map<String,Object> packPageOrders(Integer page, Integer size,List<OrderInfoDto> orderInfos){
        ArrayList<OrderInfoDto> pageOrderInfos = new ArrayList<>();
        int start = size*(page-1);
        int end   = start + size;
        int orderSize = orderInfos.size();
        if (orderSize < end && orderSize > start){
            for (int i = start; i < orderInfos.size(); i++) {
                pageOrderInfos.add(orderInfos.get(i));
            }
            HashMap<String,Object> orderInfo = new HashMap<>(2);
            orderInfo.put("pageOrderInfos",pageOrderInfos);
            orderInfo.put("total",(long) orderInfos.size());
            return orderInfo;
        }
        if (orderSize >= end) {
            for (int i = start; i < end; i++) {
                pageOrderInfos.add(orderInfos.get(i));
            }
            HashMap<String,Object> orderInfo = new HashMap<>(2);
            orderInfo.put("pageOrderInfos",pageOrderInfos);
            orderInfo.put("total", (long) orderInfos.size());
            return orderInfo;
        }
        return null;
    }

}
