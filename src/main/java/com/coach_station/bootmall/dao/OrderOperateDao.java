package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.OrderOperate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/15:45
 * @Description:
 */
public interface OrderOperateDao extends JpaRepository<OrderOperate,Integer> {
    List<OrderOperate> findByOrderNumberOrderByOperateTime(String orderNumber);

    OrderOperate findByOrderNumberAndOrderStatus(String orderNumber, Integer orderStatus);
}
