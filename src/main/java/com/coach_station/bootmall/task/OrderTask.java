package com.coach_station.bootmall.task;
import com.alibaba.fastjson.JSONObject;
import com.coach_station.bootmall.configuration.Apriori;
import com.coach_station.bootmall.entity.*;
import com.coach_station.bootmall.enumAndConst.Const;
import com.coach_station.bootmall.enumAndConst.OrderStatusEnum;
import com.coach_station.bootmall.enumAndConst.StatisticsTypeEnum;
import com.coach_station.bootmall.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: yjw
 * @Date: 2022/01/22/20:48
 * @Description:
 */
@Component
@Log4j2
public class OrderTask {

    @Autowired
    ShuttleShiftService shuttleShiftService;

    @Autowired
    ShuttleService shuttleLineService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    StatisticsOrderService statisticsOrderService;

    @Autowired
    OrderOperateService orderOperateService;

    @Autowired
    UserNewService userService;

    @Autowired
    RelationLineService relationLineService;

    //新增15日后的班次
    @Scheduled(cron = "10 0 0 * * ?")
    public void addShuttleShift() {
        log.info("---->定时任务<新增15日后的班次>进来了---------------------------------->");
        String newShiftDate = creatDate(Const.AFTER15DAY);
        String oldShiftDate = creatDate(Const.YESTERDAY);
        log.info("---->定时任务---------------------------------->" + newShiftDate);
        log.info("---->定时任务---------------------------------->" + oldShiftDate);
        List<ShuttleShift> newShuttleShifts = shuttleShiftService.findByShuttleShiftDate(newShiftDate);
        if (newShuttleShifts.size() > 0){
            return;
        }
        List<ShuttleShift> oldShuttleShifts = shuttleShiftService.findByShuttleShiftDate(oldShiftDate);
        if (oldShuttleShifts == null || oldShuttleShifts.size() == 0){
            return;
        }
        log.info("---->定时任务<新增15日后的班次>oldShuttleShifts---------------------------------->" + JSONObject.toJSONString(oldShuttleShifts));
        ArrayList<ShuttleShift> shuttleShifts = new ArrayList<>();
        for (ShuttleShift shuttleShift: oldShuttleShifts) {
            ShuttleShift newShuttleShift = new ShuttleShift();
            newShuttleShift.setShuttleShiftDate(newShiftDate);
            newShuttleShift.setShuttleShiftTime(shuttleShift.getShuttleShiftTime());
            newShuttleShift.setShuttleLineId(shuttleShift.getShuttleLineId());
            newShuttleShift.setStartStation(shuttleShift.getStartStation());
            newShuttleShift.setFinalStation(shuttleShift.getFinalStation());
            newShuttleShift.setChildTicketQuantity(shuttleShift.getChildTicketQuantity());
            newShuttleShift.setUnuseChildTicketQuantity(shuttleShift.getChildTicketQuantity());
            newShuttleShift.setTicketQuantity(shuttleShift.getTicketQuantity());
            newShuttleShift.setUnuseTicketQuantity(shuttleShift.getTicketQuantity());
            newShuttleShift.setTicketPrice(shuttleShift.getTicketPrice());
            newShuttleShift.setShuttleShiftType(shuttleShift.getShuttleShiftType());
            newShuttleShift.setLineType(shuttleShift.getLineType());
            newShuttleShift.setDuration(shuttleShift.getDuration());
            newShuttleShift.setCarId(shuttleShift.getCarId());
            newShuttleShift.setStar(shuttleShift.getStar());
            newShuttleShift.setFullLenght(shuttleShift.getFullLenght());
            newShuttleShift.setInsurancePrice(shuttleShift.getInsurancePrice());
            newShuttleShift.setRefundFee(shuttleShift.getRefundFee());
            newShuttleShift.setStationFee(shuttleShift.getStationFee());
            newShuttleShift.setStationNumber(shuttleShift.getStationNumber());
            newShuttleShift.setIsDelete(Const.NOT_DELETE);
            shuttleShifts.add(newShuttleShift);
        }
        while (true){
            try {
                List<ShuttleShift> shifts = shuttleShiftService.addShuttleShifts(shuttleShifts);
                log.info("---->定时任务<新增15日后的班次>shifts---------------------------------->" + shifts);
                if (shifts != null && shifts.size() > 0){
                    break;
                }
            }catch (Exception e){
                List<ShuttleShift> shifts = shuttleShiftService.addShuttleShifts(shuttleShifts);
                return;
            }
        }
    }

    //统计今日订单量
    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void statisticsDayOrder() {
        List<OrderStatistics> todayStatisticsInfo = statisticsOrderService.getTodayOrderQuantity();
        if (todayStatisticsInfo == null || todayStatisticsInfo.size() <= 0){
            return;
        }
        OrderStatistics monthOrderStatistics = statisticsOrderService.findByStatisticsTypeAndDate(StatisticsTypeEnum.MONTH_QUANTITY.getIndex(),String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1));
        OrderStatistics yearOrderStatistics = statisticsOrderService.findByStatisticsTypeAndDate(StatisticsTypeEnum.YEAR_QUANTITY.getIndex(),String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        Long todayOrderQuantity = 0L;
        for (OrderStatistics statisticsInfo: todayStatisticsInfo) {
            todayOrderQuantity += statisticsInfo.getOrderQuantity();
            statisticsInfo.setOrderQuantity(Const.RESET_QUANTITY);
        }
        OrderStatistics todayStatisticsOrder = new OrderStatistics();
        todayStatisticsOrder.setOrderQuantity(todayOrderQuantity);
        todayStatisticsOrder.setStatisticsType(StatisticsTypeEnum.DAY_QUANTITY.getIndex());
        todayStatisticsOrder.setShardingKey(Const.DEFAULT_SHARDINGKEY);
        todayStatisticsOrder.setDate(creatDate(Const.YESTERDAY));
        Long monthOrderQuantity = monthOrderStatistics.getOrderQuantity() + todayOrderQuantity;
        monthOrderStatistics.setOrderQuantity(monthOrderQuantity);
        Long yearOrderQuantity = monthOrderStatistics.getOrderQuantity() + todayOrderQuantity;
        yearOrderStatistics.setOrderQuantity(yearOrderQuantity);
        todayStatisticsInfo.add(todayStatisticsOrder);
        todayStatisticsInfo.add(monthOrderStatistics);
        todayStatisticsInfo.add(yearOrderStatistics);
        List<OrderStatistics> statisticsOrderResults = statisticsOrderService.saveAll(todayStatisticsInfo);
        if (statisticsOrderResults == null || statisticsOrderResults.size() == 0){
            statisticsDayOrder();
        }
    }

    //统计月订单量
    @Scheduled(cron = "0 0 23 * * ?")
    public void addMonthOrderStatistics() {
        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            OrderStatistics orderStatistics = new OrderStatistics();
            orderStatistics.setStatisticsType(StatisticsTypeEnum.MONTH_QUANTITY.getIndex());
            orderStatistics.setDate(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+2));
            orderStatistics.setShardingKey(Const.DEFAULT_SHARDINGKEY);
            orderStatistics.setOrderQuantity(Const.RESET_QUANTITY);
            OrderStatistics monthOrderStatistics = statisticsOrderService.findByStatisticsTypeAndDate(StatisticsTypeEnum.MONTH_QUANTITY.getIndex(), String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 2));
            if (monthOrderStatistics != null){
                return;
            }
            OrderStatistics orderStatisticsResult = statisticsOrderService.save(orderStatistics);
            if (orderStatisticsResult == null){
                addMonthOrderStatistics();
            }
        }
    }

    //统计年订单量
    @Scheduled(cron = "1 0 23 * * ?")
    public void addYearOrderStatistics() {
        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.MONTH)+1 == c.getActualMaximum(Calendar.MONTH)+1){
            if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
                OrderStatistics orderStatistics = new OrderStatistics();
                orderStatistics.setStatisticsType(StatisticsTypeEnum.YEAR_QUANTITY.getIndex());
                orderStatistics.setOrderQuantity(Const.RESET_QUANTITY);
                orderStatistics.setDate(String.valueOf(c.get(Calendar.YEAR)+1));
                orderStatistics.setShardingKey(Const.DEFAULT_SHARDINGKEY);
                OrderStatistics yearOrderStatistics = statisticsOrderService.findByStatisticsTypeAndDate(StatisticsTypeEnum.YEAR_QUANTITY.getIndex(), String.valueOf(c.get(Calendar.YEAR) + 1));
                if (yearOrderStatistics != null){
                    return;
                }
                OrderStatistics yearOrderStatisticsResult = statisticsOrderService.save(orderStatistics);
                if (yearOrderStatisticsResult == null){
                    addYearOrderStatistics();
                }
            }
        }
    }

    //定时检测未支付订单
    @Scheduled(cron = "0 */5 * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void updateArrearageOrderStatus() {
        Long time = System.currentTimeMillis() - Const.HALF_HOUR_TIMESTAMP;
        List<OrderInfo> arrearageOrders = orderInfoService.findByOrderStatusAndCreateTimeLessThan(OrderStatusEnum.ARREARAGE.getIndex(),time);
        if (arrearageOrders == null || arrearageOrders.size() == 0){
            return;
        }
        HashMap<Long, Integer> arrearageShuttleShift = new HashMap<>();
        ArrayList<OrderOperate> orderOperates = new ArrayList<>();
        for (OrderInfo orderInfo: arrearageOrders) {
            if (arrearageShuttleShift.containsKey(orderInfo.getShuttleShiftId())){
                Integer ticketQuantity = arrearageShuttleShift.get(orderInfo.getShuttleShiftId());
                arrearageShuttleShift.put(orderInfo.getShuttleShiftId(),ticketQuantity+1);
            }else {
                arrearageShuttleShift.put(orderInfo.getShuttleShiftId(),1);
            }
            orderInfo.setOrderStatus(OrderStatusEnum.CANCEL.getIndex());
            OrderOperate orderOperate = new OrderOperate();
            orderOperate.setOperateTime(System.currentTimeMillis());
            orderOperate.setOrderNumber(orderInfo.getOrderNumber());
            orderOperate.setOrderStatus(OrderStatusEnum.CANCEL.getIndex());
            orderOperates.add(orderOperate);
        }
        List<ShuttleShift> shuttleShifts = shuttleShiftService.findByShiftIdIn(arrearageShuttleShift.keySet());
        if (shuttleShifts == null || shuttleShifts.size() == 0){
            return;
        }
        for (ShuttleShift shuttleShift: shuttleShifts) {
            Integer unuseTicketQuantity = shuttleShift.getUnuseTicketQuantity() + arrearageShuttleShift.get(shuttleShift.getShiftId());
            shuttleShift.setUnuseTicketQuantity(unuseTicketQuantity);
        }
        try {
            shuttleShiftService.saveAll(shuttleShifts);
            orderInfoService.saveAll(arrearageOrders);
            orderOperateService.saveAll(orderOperates);
        }catch (Exception e){
            updateArrearageOrderStatus();
        }
    }

    //每日推荐算法
    @Scheduled(cron = "0 1 3 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void updateRelationLine() throws IOException {
        ArrayList<String> userLines = new ArrayList<>();
        List<User> users = userService.findAllUsers();
        for (User user:users) {
            if (user.getTotalRegionsId() == null || user.getTotalRegionsId().length() == 0){
                continue;
            }
            userLines.add(user.getTotalRegionsId());
        }
        relationLineService.deleteAll();
        List<RelationLine> relationLines = Apriori.getRelationLines(userLines);
        relationLineService.insertRelationLines(relationLines);
    }

    //统计线路的总量
    @Scheduled(cron = "0 1 3 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void statisticsDayOrderToShuttleLine() throws IOException {
        ArrayList<String> userLines = new ArrayList<>();
        List<User> users = userService.findAllUsers();
        for (User user:users) {
            if (user.getTotalRegionsId() == null || user.getTotalRegionsId().length() == 0){
                continue;
            }
            userLines.add(user.getTotalRegionsId());
        }
        relationLineService.deleteAll();
        List<RelationLine> relationLines = Apriori.getRelationLines(userLines);
        relationLineService.insertRelationLines(relationLines);
    }
    
    String creatDate(Integer day){
        Calendar NewCalendar = Calendar.getInstance();
        NewCalendar.add(Calendar.DATE,day);
        DateFormat date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  date.format(NewCalendar.getTime());
    }
}