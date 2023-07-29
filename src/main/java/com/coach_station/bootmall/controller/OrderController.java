package com.coach_station.bootmall.controller;

import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.service.OrderService;
import com.coach_station.bootmall.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: yjw
 * @Date: 2022/01/11/16:05
 * @Description:
 */
@RequestMapping("/order")
@Controller
@CrossOrigin
public class OrderController {

    @Autowired
    OrderService orderService;

    // 查询所有订单接口
    @GetMapping("/getOrderInfo")
    @ResponseBody
    public ResultOfDataPage getOrderInfo  (
            @RequestParam(value = "start_time",required = false) String startTime,
            @RequestParam(value = "end_time",required = false) String endTime,
            @RequestParam(value = "order_status",defaultValue = "all") String orderStatus,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "size",defaultValue = "8") Integer size) throws ParseException {
        Map<String, Object> orderInfo = orderService.getOrderInfo(startTime, endTime, orderStatus, page, size);
        if (orderInfo == null){
            return ResultOfDataPage.setResult(ResultCodeEnum.ORDER_GETORDERINFO_ERROR);
        }
        return ResultOfDataPage.ok().
                data("order_list",orderInfo.get("OrderInfoVos")).
                setDataInfo(page,size,(Long) orderInfo.get("total"));
    }

    // 获取一个主订单下子订单详情接口（所有订单、待使用单、待支付单、退款订单通用接口）
    @GetMapping("/getOrderDetailInfo")
    @ResponseBody
    public Result getOrderDetailInfo  (
            @RequestParam(value = "master_order_number") String masterOrderNumber) throws ParseException {
        List<OrderDetailInfoVo> orderDetailInfos = orderService.getOrderDetailInfo(masterOrderNumber);
        if (orderDetailInfos == null){
            return Result.setResult(ResultCodeEnum.ORDER_GETORDERDETAILINFO_ERROR);
        }
        return Result.ok().
                data("order_detail_list",orderDetailInfos);
    }


    // 取消订单接口
    @GetMapping("/cancelOrder")
    @ResponseBody
    public Result cancelOrder  (
            @RequestParam(value = "master_order_number") String orderNumber) throws ParseException {
        ResultCodeEnum code = orderService.cancelOrder(orderNumber);
        return Result.setResult(code);
    }

    // 提交订单并预订订单接口
    @PostMapping("/bookOrder")
    @ResponseBody
    public Result bookOrder  (@RequestBody BookOrderVo bookOrderVo) {
        try {
            Map<String,Object> result = orderService.bookOrder(bookOrderVo);
            if (!((ResultCodeEnum) result.get("code")).getSuccess()){
                return Result.setResult(((ResultCodeEnum) result.get("code")));
            }
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("order_info",((BookOrderResultVo) result.get("result")).getOrderInfos());
            resultMap.put("master_order_number",((BookOrderResultVo) result.get("result")).getMasterOrderNumber());
            resultMap.put("master_total_amount",((BookOrderResultVo) result.get("result")).getMasterTotalAmount());
            return Result.ok().data(resultMap);
        }catch (Exception e){
            return Result.setResult(ResultCodeEnum.ORDER_BOOKORDER_TICKETNOTENOUGH_ERROR);
        }
    }

    // 查看支付是否成功接口
    @GetMapping("/payOrderVerify")
    @ResponseBody
    public Result payOrderVerify  (@RequestParam(value = "master_order_number") String masterOrderNumber) {
        Map<String, Object> payOrderVo = orderService.payOrderVerify(masterOrderNumber);
        if (payOrderVo == null){
            return Result.error();
        }
        return Result.ok().data(payOrderVo);
    }

    // 获取乘车码接口
    @GetMapping("/getRideCode")
    @ResponseBody
    public Result getRideCode  (@RequestParam(value = "master_order_number") String masterOrderNumber) {
        String rideCode = orderService.getRideCode(masterOrderNumber);
        if (rideCode == null || rideCode.length() < 6){
            return Result.error();
        }
        return Result.ok().data("ride_code",rideCode);
    }
}
