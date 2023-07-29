package com.coach_station.bootmall.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.coach_station.bootmall.configuration.AlipayConfig;
import com.coach_station.bootmall.entity.OrderInfo;
import com.coach_station.bootmall.entity.OrderOperate;
import com.coach_station.bootmall.entity.ShuttleShift;
import com.coach_station.bootmall.enumAndConst.Const;
import com.coach_station.bootmall.enumAndConst.OrderStatusEnum;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.qrcode.QRCodeUtil;
import com.coach_station.bootmall.qrcode.QrCodeResponse;
import com.coach_station.bootmall.qrcode.QrResponse;
import com.coach_station.bootmall.util.GenerateNum;
import com.coach_station.bootmall.vo.PayVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 徐柯
 * @Title:
 * @Package
 * @Description:
 * @date 2021/3/2922:18
 */
@Service
public class AlipayServiceImpl implements  AlipayService{


    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private ShuttleShiftService shuttleShiftService;

    @Autowired
    private OrderOperateService orderOperateService;
//    @Autowired
//    private ProductCourseService productCourseService;
    
    /**
     * @return byte[]
     * @Author xuke
     * @Description 阿里支付接口
     * @Date 1:05 2020/9/9
     * @Param [payVo]
     **/
    public BufferedImage alipay(String masterOrderNumber){
        try {
            // 业务数据
            // 1：支付的用户
            Long userId = (Long)getSessionAttribute("userId");
            if ( userId == null || userId <= 0){
                return null;
                //todo 打日志
            }
            System.out.println(masterOrderNumber);
            OrderInfo orderInfo = orderInfoService.findByMasterOrderNumberAndUserId(masterOrderNumber,userId);
            if(orderInfo==null)return null;
            // 2: 支付金额
            String money = orderInfo.getTotalAmount().toString();
            // 3: 支付的产品
            String title = "车票订单号" + masterOrderNumber;
            // 4: 支付的订单编号
            System.out.println("订单哈" + masterOrderNumber);
            // 5：支付宝携带的参数在回调中可以通过request获取
            JSONObject json = new JSONObject();
            json.put("userId", userId);
            json.put("orderNumber", masterOrderNumber);
            json.put("money", money);
            String params = json.toString();

            // 支付信息的参数
            // 6：设置支付相关的信息
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            model.setOutTradeNo(masterOrderNumber); // 自定义订单号
            model.setTotalAmount(money);// 支付金额
            model.setSubject(title);// 支付的产品名称
            model.setBody(params);// 支付的请求体参数
            model.setTimeoutExpress("30m");// 支付的超时时间
            QrCodeResponse qrCodeResponse = qrcodePay(model);


            //7 二维码合成
            String logopath = "";
            try {
                logopath = ResourceUtils.getFile("classpath:favicon.png").getAbsolutePath();
            }catch (Exception ex){
                logopath = new java.io.File("/www/web/favicon.png").getAbsolutePath();
            }

            return QRCodeUtil.encode(qrCodeResponse.getQr_code(), logopath, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultCodeEnum alipayRefundOrder(String orderNumber) {
        try {
            // 业务数据
            // 1：支付的用户
            Long userId = (Long)getSessionAttribute("userId");
            if ( userId == null || userId <= 0){
                return null;
                //todo 打日志
            }
            System.out.println(orderNumber);
            OrderInfo orderInfo = orderInfoService.findByOrderNumberAndUserId(orderNumber,userId);
            if(orderInfo==null)return ResultCodeEnum.ORDER_REFUNDORDER_ERROR;
            ShuttleShift shuttleShift = shuttleShiftService.findByShiftId(orderInfo.getShuttleShiftId());
            if (shuttleShift == null){
                return ResultCodeEnum.ORDER_REFUNDORDER_ERROR;
            }
            JSONObject bizContent = new JSONObject();
            if (orderInfo.getOrderType().equals(Const.MASTER_ORDER)){
                bizContent.put("out_trade_no", orderInfo.getOrderNumber());
                bizContent.put("refund_amount", orderInfo.getTotalAmount().subtract(shuttleShift.getRefundFee().multiply(BigDecimal.valueOf(orderInfo.getSlaveOrderQuantity()))));
                bizContent.put("out_request_no", orderInfo.getOrderNumber());
            }
            if (orderInfo.getOrderType().equals(Const.CHILD_ORDER)){
                bizContent.put("out_trade_no", orderInfo.getMasterOrderNumber());
                bizContent.put("refund_amount", orderInfo.getTotalAmount().subtract(shuttleShift.getRefundFee()));
                bizContent.put("out_request_no", orderInfo.getRideCode());
            }

            AlipayClient alipayClient = getAlipayClient();
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

            System.out.println(JSONObject.toJSONString(bizContent));
            request.setBizContent(bizContent.toString());
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            System.out.println(JSONObject.toJSONString(response));
            if(response.isSuccess()){
                modifyOrderStatus(orderInfo,shuttleShift);
                return ResultCodeEnum.SUCCESS;
            } else {
                return ResultCodeEnum.ORDER_REFUNDORDER_ERROR;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResultCodeEnum.ORDER_REFUNDORDER_ERROR;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    void modifyOrderStatus(OrderInfo orderInfo, ShuttleShift shuttleShift) {
        List<OrderInfo> orderInfos = new ArrayList<>();
        ArrayList<OrderOperate> orderOperates = new ArrayList<>();
        if (orderInfo.getOrderType().equals(Const.MASTER_ORDER)){
            List<OrderInfo> masterOrderInfos = orderInfoService.findByOrderNumberAndMasterOrderNumberAndUserId(orderInfo.getOrderNumber(), orderInfo.getUserId());
            for (OrderInfo order: masterOrderInfos) {
                OrderOperate orderOperate = new OrderOperate();
                order.setOrderStatus(OrderStatusEnum.REFUND_ALL.getIndex());
                order.setRideCode("-1");
                order.setRefundAmount(orderInfo.getTotalAmount().subtract(shuttleShift.getRefundFee()));
                if (order.getOrderType().equals(Const.MASTER_ORDER)){
                    order.setRefundAmount(orderInfo.getTotalAmount().subtract(shuttleShift.getRefundFee().multiply(BigDecimal.valueOf(orderInfo.getSlaveOrderQuantity()))));
                }

                orderOperate.setOrderStatus(OrderStatusEnum.REFUND_ALL.getIndex());
                orderOperate.setOrderNumber(order.getOrderNumber());
                orderOperate.setOperateTime(System.currentTimeMillis());
                orderOperates.add(orderOperate);
            }
            orderInfos = masterOrderInfos;
        }else {
            OrderInfo masterOrderInfo = orderInfoService.findByMasterOrderNumberAndUserId(orderInfo.getMasterOrderNumber(), orderInfo.getUserId());
            orderInfo.setOrderStatus(OrderStatusEnum.REFUND_ALL.getIndex());
            orderInfo.setRefundAmount(orderInfo.getTotalAmount().subtract(shuttleShift.getRefundFee()));
            orderInfo.setRideCode("-1");
            OrderOperate orderOperate = new OrderOperate();
            orderOperate.setOrderStatus(OrderStatusEnum.REFUND_ALL.getIndex());
            orderOperate.setOrderNumber(orderInfo.getOrderNumber());
            orderOperate.setOperateTime(System.currentTimeMillis());
            orderOperates.add(orderOperate);
            orderInfos.add(orderInfo);

            System.out.println(orderInfo.getRefundAmount());
            OrderOperate masterOrderOperate = new OrderOperate();
            masterOrderInfo.setOrderStatus(OrderStatusEnum.REFUND_PART.getIndex());
            orderInfo.setRefundAmount(orderInfo.getTotalAmount().subtract(shuttleShift.getRefundFee()));
            masterOrderOperate.setOrderStatus(OrderStatusEnum.REFUND_PART.getIndex());
            masterOrderOperate.setOrderNumber(masterOrderInfo.getOrderNumber());
            masterOrderOperate.setOperateTime(System.currentTimeMillis());
            orderOperates.add(masterOrderOperate);
            orderInfos.add(masterOrderInfo);
        }

        List<OrderInfo> orderInfosResult = orderInfoService.saveAll(orderInfos);
        List<OrderOperate> orderOperatesResult = orderOperateService.saveAll(orderOperates);
    }

    @Override
    public ResultCodeEnum refundOrderVerify(String masterOrderNumber) {
//        AlipayClient alipayClient = getAlipayClient();
//        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
//        JSONObject bizContent = new JSONObject();
//        bizContent.put("trade_no", "2021081722001419121412730660");
//        bizContent.put("out_request_no", "HZ01RF001");
//
//        request.setBizContent(bizContent.toString());
//        AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
//        if(response.isSuccess()){
//            System.out.println("调用成功");
//        } else {
//            System.out.println("调用失败");
//        }
        return null;
    }


    /**
     * 扫码运行代码
     * 验签通过返回QrResponse
     * 失败打印日志信息
     * 参考地址：https://opendocs.alipay.com/apis/api_1/alipay.trade.app.pay
     *
     * @param model
     * @return
     */
    public QrCodeResponse qrcodePay(AlipayTradePrecreateModel model) {
        // 1: 获取阿里请求客户端
        AlipayClient alipayClient = getAlipayClient();
        // 2: 获取阿里请求对象
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        // 3：设置请求参数的集合，最大长度不限
        request.setBizModel(model);
        // 设置异步回调地址
        request.setNotifyUrl(alipayConfig.getNotify_url());
        // 设置同步回调地址
        request.setReturnUrl(alipayConfig.getReturn_url());
        AlipayTradePrecreateResponse alipayTradePrecreateResponse = null;
        try {
            alipayTradePrecreateResponse = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        QrResponse qrResponse = JSON.parseObject(alipayTradePrecreateResponse.getBody(), QrResponse.class);
        System.out.println(JSONObject.toJSONString(qrResponse));
        return qrResponse.getAlipay_trade_precreate_response();
    }
    /**
     * 获取AlipayClient对象
     *
     * @return
     */
    private AlipayClient getAlipayClient() {
        AlipayClient alipayClient =
                new DefaultAlipayClient(alipayConfig.getGatewayUrl(), alipayConfig.getApp_id(), alipayConfig.getMerchant_private_key(),
                        "JSON", alipayConfig.getCharset(), alipayConfig.getAlipay_public_key(), alipayConfig.getSign_type()); //获得初始化的AlipayClient
        return alipayClient;
    }

    private Object getSessionAttribute(String attributeKey){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return session.getAttribute(attributeKey);
    }
}