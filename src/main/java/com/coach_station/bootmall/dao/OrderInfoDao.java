package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.OrderInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/21:18
 * @Description:
 */
public interface OrderInfoDao extends JpaRepository<OrderInfo,Integer> {
    List<OrderInfo> findByOrderNumberOrMasterOrderNumberAndUserId(String orderNumber, String masterOrderNumber, Long userId);

    OrderInfo findByOrderNumberAndUserIdAndAndOrderType(String masterOrderNumber, Long userId, Integer orderType);

    List<OrderInfo> findByRideCode(String rideCode);

    List<OrderInfo> findByOrderStatusAndCreateTimeLessThan(Integer orderStatus, Long time);

    OrderInfo findByOrderNumber(String masterOrderNumber);

    OrderInfo findByOrderNumberAndUserId(String masterOrderNumber, Long userId);

    List<OrderInfo> findAllByUserIdAndAndOrderStatusAndOrderTypeAndIsDelete(Long userId,Integer orderStatus,Integer orderType, Integer isDelete);

    List<OrderInfo> findAllByPassengerIdInAndOrderStatus(List<Long> passengers,Integer orderStatus);

    OrderInfo findByOrderId(Long orderId);

    Page<OrderInfo> findByUserIdAndOrderStatusAndAndOrderType(Long userId, Integer orderStatus, Integer orderType, Pageable pageable);

    List<OrderInfo> findAllByOrderType(Integer orderType);

    List<OrderInfo> findAllByOrderTypeAndCreateTimeGreaterThanEqual(Integer orderType, Long creatTime);
}
