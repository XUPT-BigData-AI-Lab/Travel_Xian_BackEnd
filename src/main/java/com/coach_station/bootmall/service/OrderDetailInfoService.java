package com.coach_station.bootmall.service;

import com.alibaba.fastjson.JSONObject;
import com.coach_station.bootmall.dao.OrderDetailInfoDao;
import com.coach_station.bootmall.dto.OrderDetailInfoDto;
import com.coach_station.bootmall.dto.OrderInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: yjw
 * @Date: 2022/01/13/13:50
 * @Description:
 */
@Service
public class OrderDetailInfoService {

    @Autowired
    OrderDetailInfoDao orderDetailInfoDao;

    public List<OrderDetailInfoDto> findOrderDetailInfoByMasterOrderNumber(Long userId, Integer orderType, String masterOrderNumber){
        List<OrderDetailInfoDto> orderDetailInfos = orderDetailInfoDao.findOrderDetailInfoByMasterOrderNumber(masterOrderNumber,userId, orderType);
        System.out.println(JSONObject.toJSONString(orderDetailInfos));
        if (orderDetailInfos == null || orderDetailInfos.size() == 0){
            return null;
            //todo 打日志
        }
        return orderDetailInfos;
    }

}
