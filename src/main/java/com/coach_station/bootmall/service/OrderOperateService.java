package com.coach_station.bootmall.service;

import com.coach_station.bootmall.dao.OrderOperateDao;
import com.coach_station.bootmall.dto.OrderDetailInfoDto;
import com.coach_station.bootmall.entity.OrderInfo;
import com.coach_station.bootmall.entity.OrderOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/15:47
 * @Description:
 */
@Service
public class OrderOperateService {

    @Autowired
    OrderOperateDao orderOperateDao;

    public List<OrderOperate> findByOrderNumber(String orderNumber){
        List<OrderOperate> orderDetailInfos = orderOperateDao.findByOrderNumberOrderByOperateTime(orderNumber);
        if (orderDetailInfos == null || orderDetailInfos.size() == 0){
            return null;
            //todo 打日志
        }
        return orderDetailInfos;
    }

    public OrderOperate findByOrderNumberAndOrderStatus(String orderNumber, Integer orderStatus){
        return orderOperateDao.findByOrderNumberAndOrderStatus(orderNumber, orderStatus);
    }

    public List<OrderOperate> saveAll(Iterable<OrderOperate> orderOperates){
        return orderOperateDao.saveAll(orderOperates);
    }

}
