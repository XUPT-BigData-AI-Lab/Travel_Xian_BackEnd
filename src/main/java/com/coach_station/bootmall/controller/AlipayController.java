package com.coach_station.bootmall.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.coach_station.bootmall.configuration.AlipayConfig;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.service.AlipayService;
import com.coach_station.bootmall.service.OrderService;
import com.coach_station.bootmall.util.GenerateNum;
import com.coach_station.bootmall.vo.PayVo;
import com.coach_station.bootmall.vo.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @description:
 * @author: xuke
 * @time: 2021/4/2 0:53
 */
@Controller
@CrossOrigin
@Log4j2
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @Autowired
    OrderService orderService;

    @GetMapping("/order/payOrder")
    @ResponseBody
    public void alipay(@RequestParam(value = "master_order_number") String masterOrderNumber,HttpServletResponse res) throws IOException, AlipayApiException {
        BufferedImage img = alipayService.alipay(masterOrderNumber);
        if (img == null){
            return;
        }
        setImage(res,img);
    }

    /**
     * 定义支付回调的地址
     *
     * 1：第一步：打成一个jar 发布正式服务器
     * 2：第二步：购买一个域名：https:/www.kuangstudy.com/
     * 3：第三步：部署的项目到服务器上。java -jar ksd-alipay.jar >>1.txt &
     * 4：第四步：获取真实的回调地址  https:/www.kuangstudy.com/alipay/notifyUrl?body=&param&
     */
    @ResponseBody
    @RequestMapping("/alipay/notifyUrl")
    public String notify_url(HttpServletRequest request) throws Exception {
        // 1: 调用支付回调
        boolean result = alipayCallback(request);
        if (result) {
            return "success"; // 请不要修改或删除
        } else {
            // 验证失败
            return "fail";
        }
    }

    // 获取一个主订单下子订单详情接口（所有订单、待使用单、待支付单、退款订单通用接口）
    @GetMapping("/order/refundOrder")
    @ResponseBody
    public Result refundOrder  (
            @RequestParam(value = "order_number") String orderNumber,
            @RequestParam(value = "refund_type") String refundType) throws ParseException {
        try {
            ResultCodeEnum code = alipayService.alipayRefundOrder(orderNumber);
            if (!code.getSuccess()){
                return Result.setResult(code);
            }
        }catch (Exception e){
            return Result.setResult(ResultCodeEnum.ORDER_REFUNDORDER_ERROR);
        }
        return Result.setResult(ResultCodeEnum.SUCCESS);
    }

    private boolean alipayCallback(HttpServletRequest request) throws UnsupportedEncodingException {
        // 1:获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, new String(valueStr.getBytes("ISO-8859-1"), "UTF-8"));
        }
        //2：计算得出通知验证结果
        log.info("1：---->获取支付宝回传的参数---------------------------------->" + params);
        // 返回公共参数
        String tradeno = params.get("trade_no");
        //支付返回的请求参数body
        String bodyJson = params.get("body");
        log.info("3---->:【支付宝】交易的参数信息是：{}，流水号是：{}", bodyJson, tradeno);
        try {
            JSONObject bodyJsonObject = JSONObject.parseObject(bodyJson);
            System.out.println("入参Json数据：" + JSONObject.toJSONString(bodyJsonObject));
            ResultCodeEnum codeEnum = orderService.payOrderCallBack(bodyJsonObject,tradeno);
            if (!codeEnum.getSuccess())
                return false;
            // 课程支付成功，保存的订单交易明

//            payCommonService.awardPay(bodyJsonObject,"1",orderNumber,tradeno,"1");

        } catch (Exception ex) {
            log.info("支付宝支付出现了异常.....");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void setImage(HttpServletResponse res,BufferedImage img) throws IOException {
        res.setContentType("image/png");
        OutputStream os = res.getOutputStream();
        ImageIO.write(img, "JPEG", os);
        os.close();
    }
}
