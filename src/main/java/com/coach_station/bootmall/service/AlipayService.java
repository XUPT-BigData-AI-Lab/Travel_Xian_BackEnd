package com.coach_station.bootmall.service;

import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.vo.PayVo;

import java.awt.image.BufferedImage;

/**
 * @author 徐柯
 * @Title:
 * @Package
 * @Description:
 * @date 2021/3/2922:18
 */
public interface AlipayService {
    /**
     * @return byte[]
     * @Author xuke
     * @Description 阿里支付接口
     * @Date 1:05 2020/9/9
     * @Param [payVo]
     **/
    BufferedImage alipay(String masterOrderNumber);

    ResultCodeEnum alipayRefundOrder(String masterOrderNumber);

    ResultCodeEnum refundOrderVerify(String masterOrderNumber);
}