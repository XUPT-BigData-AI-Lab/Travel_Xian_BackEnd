package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.OrderInfoDao;
import com.coach_station.bootmall.entity.OrderInfo;
import com.coach_station.bootmall.enumAndConst.Const;
import com.coach_station.bootmall.enumAndConst.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/21:22
 * @Description:
 */
@Service
public class OrderInfoService {
    @Autowired
    OrderInfoDao orderInfoDao;

    public List<OrderInfo> findByOrderNumberAndMasterOrderNumberAndUserId(String orderNumber, Long userId){
        return orderInfoDao.findByOrderNumberOrMasterOrderNumberAndUserId(orderNumber, orderNumber, userId);
    }

    public List<OrderInfo> saveAll(Iterable<OrderInfo> orderInfos){
        return orderInfoDao.saveAll(orderInfos);
    }

    public OrderInfo findByMasterOrderNumberAndUserId(String masteOrderNumber, Long userId){
        return orderInfoDao.findByOrderNumberAndUserIdAndAndOrderType(masteOrderNumber,userId, Const.MASTER_ORDER);
    }

    public List<OrderInfo> findByRideCode(String rideCode){
        return orderInfoDao.findByRideCode(rideCode);
    }

    public OrderInfo findByOrderNumber(String masterOrderNumber){
        return orderInfoDao.findByOrderNumber(masterOrderNumber);
    }

    public List<OrderInfo> findByOrderStatusAndCreateTimeLessThan(Integer orderStatus, Long time){
        return orderInfoDao.findByOrderStatusAndCreateTimeLessThan(orderStatus,time);
    }

    public OrderInfo findByOrderNumberAndUserId(String masterOrderNumber, Long userId){
        return orderInfoDao.findByOrderNumberAndUserId(masterOrderNumber,userId);
    }

    public List<OrderInfo> findAllByUserIdAndAndOrderStatusAndOrderTypeAndIsDelete(Long userId,Integer orderStatus,Integer orderType, Integer isDelete){
        return orderInfoDao.findAllByUserIdAndAndOrderStatusAndOrderTypeAndIsDelete(userId,orderStatus,orderType,isDelete);
    }

    List<OrderInfo> findAllByPassengerIdInAndOrderStatus(List<Long> passengers,Integer orderStatus){
        return orderInfoDao.findAllByPassengerIdInAndOrderStatus(passengers,orderStatus);
    }

    OrderInfo findByOrderId(Long orderId){
        return orderInfoDao.findByOrderId(orderId);
    }

    OrderInfo save(OrderInfo orderInfo){
        return orderInfoDao.save(orderInfo);
    }

    Page<OrderInfo> findByUserId(Long userId, Pageable pageable){
        return orderInfoDao.findByUserIdAndOrderStatusAndAndOrderType(userId, OrderStatusEnum.STANDBY.getIndex(), Const.MASTER_ORDER, pageable);
    }

    public List<OrderInfo> findAllByOrderType(Integer orderType){
        return orderInfoDao.findAllByOrderType(orderType);
    }

    public List<OrderInfo> findAllByOrderTypeAndCreateTimeGreaterThanEqual(Integer orderType, Long creatTime){
        return orderInfoDao.findAllByOrderTypeAndCreateTimeGreaterThanEqual(orderType,creatTime);
    }
}
