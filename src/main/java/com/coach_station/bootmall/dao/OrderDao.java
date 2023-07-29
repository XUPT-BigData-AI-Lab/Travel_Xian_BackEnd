package com.coach_station.bootmall.dao;

import com.alibaba.fastjson.JSONObject;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.vo.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: yjw
 * @Date: 2022/01/12/14:19
 * @Description:
 */
public interface OrderDao {

    Map<String,Object> getOrderInfo(String startTime, String endTime, String orderStatus, Integer page, Integer size) throws ParseException;

    List<OrderDetailInfoVo> getOrderDetailInfo(String masterOrderNumber) throws ParseException;

    ResultCodeEnum refundOrder(String orderNumber, String refundType);

    ResultCodeEnum cancelOrder(String orderNumber);

    Map<String,Object> bookOrder(BookOrderVo bookOrderVo);

    ResultCodeEnum payOrderCallBack(JSONObject bodyJsonObject,String tradeno);

    Map<String, Object> payOrderVerify(String masterOrderNumber);

    String getRideCode(String masterOrderNumber);

}
