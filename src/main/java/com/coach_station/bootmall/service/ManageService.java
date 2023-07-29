package com.coach_station.bootmall.service;

import com.alibaba.fastjson.JSONObject;
import com.coach_station.bootmall.dao.ManageDao;
import com.coach_station.bootmall.entity.OrderInfo;
import com.coach_station.bootmall.entity.Passenger;
import com.coach_station.bootmall.entity.ShuttleLine;
import com.coach_station.bootmall.entity.ShuttleShift;
import com.coach_station.bootmall.enumAndConst.*;
import com.coach_station.bootmall.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.LinkOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/04/12/14:47
 * @Description:
 */
@Service
public class ManageService implements ManageDao {
    @Autowired
    ShuttleShiftService shuttleShiftService;

    @Autowired
    ShuttleService shuttleService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    PassengerService passengerService;

    @Override
    public ResultOfDataPage getShuttleInfoList(GetShuttleInfoListRequestVo requestVo) {
        ResultCodeEnum checkResultCode = checkGetShuttleInfoListParams(requestVo);
        if (!checkResultCode.getSuccess()){
            return ResultOfDataPage.setResult(checkResultCode).setDataInfo(0,0, 0L);
        }
        ShuttleLine shuttleLine = shuttleService.findByStartRegionIdAndAndFinalRegionId(requestVo.getStartRegionId(), requestVo.getFinalRegionId());
        if (shuttleLine == null || shuttleLine.getLineId().equals(0L)){
            return ResultOfDataPage.setResult(ResultCodeEnum.MANAGE_WITHOUTLINE_ERROR).setDataInfo(0,0, 0L);
        }
        Sort sort = new Sort(Sort.Direction.ASC,"shiftId");
        Pageable pageable = PageRequest.of(requestVo.getPage()-1,requestVo.getSize(),sort);
        Page<ShuttleShift> shuttleShifts = shuttleShiftService.findByShuttleLineIdAndShuttleShiftDate(shuttleLine.getLineId(), requestVo.getShuttleShiftDate(), pageable);
        if (shuttleShifts == null || shuttleShifts.getContent().size() == 0){
            return ResultOfDataPage.setResult(ResultCodeEnum.MANAGE_WITHOUTSHUTTLE_ERROR).setDataInfo(0,0, 0L);
        }
        return packShuttleShifts(shuttleShifts,requestVo.getPage(),requestVo.getSize(), shuttleLine.getStartRegion(), shuttleLine.getFinalRegion());
    }

    @Override
    public ResultCodeEnum createShuttleInfo(ShuttleInfoManageVo requestVo) {
        ResultCodeEnum checkResultCode = checkCreateShuttleInfoParams(requestVo);
        if (!checkResultCode.getSuccess()){
            return checkResultCode;
        }
        ShuttleLine shuttleLine = shuttleService.findByStartRegionAndFinalRegion(requestVo.getStartRegion(), requestVo.getFinalRegion());
        if (shuttleLine == null){
            return ResultCodeEnum.MANAGE_WITHOUTLINE_ERROR;
        }
        ShuttleShift shuttleShift = new ShuttleShift();
        BeanUtils.copyProperties(requestVo,shuttleShift);
        shuttleShift.setShuttleLineId(shuttleLine.getLineId());
        shuttleShift.setShuttleShiftType(ShuttleShiftTypeEnum.getShuttleShiftTypeIndex(requestVo.getShuttleShiftType()));
        shuttleShift.setLineType(LineTypeEnum.getLineTypeIndex(requestVo.getLineType()));
        shuttleShift.setCarId(1L);
        shuttleShift.setStar(0);
        shuttleShift.setIsDelete(ShuttleShiftStatusEnum.getShuttleShiftStatusIndex(requestVo.getStatus()));

        ShuttleShift saveResult = shuttleShiftService.save(shuttleShift);
        if (saveResult == null){
            return ResultCodeEnum.MANAGE_SAVESHUTTLE_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public ResultCodeEnum deleteShuttleInfo(Long shuttleId) {
        if (shuttleId == null || shuttleId <= 0L){
            return ResultCodeEnum.MANAGE_PARAM_ERROR;
        }
        Integer deleteResult = shuttleShiftService.deleteByShiftId(shuttleId);
        if (deleteResult != 1){
            return ResultCodeEnum.MANAGE_DELETESHUTTLE_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public ResultCodeEnum changeShuttleStatus(Long shuttleId) {
        if (shuttleId == null || shuttleId <= 0L){
            return ResultCodeEnum.MANAGE_PARAM_ERROR;
        }
        ShuttleShift shuttleShift = shuttleShiftService.findByShiftId(shuttleId);
        if (shuttleShift == null){
            return ResultCodeEnum.MANAGE_WITHOUTSHUTTLE_ERROR;
        }
        shuttleShift.setIsDelete(ShuttleShiftStatusEnum.changeShuttleShiftStatus(shuttleShift.getIsDelete()));
        ShuttleShift saveResult = shuttleShiftService.save(shuttleShift);
        if (saveResult == null){
            return ResultCodeEnum.MANAGE_SAVESHUTTLE_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public Result getOrderList(getOrderRequestVo requestVo) {
        ResultCodeEnum checkResultCode = checkGetOrderListParams(requestVo);
        if (!checkResultCode.getSuccess()){
            return Result.setResult(checkResultCode);
        }
        List<Passenger> passengers = passengerService.findAllByCardNumberAndCardTypeAndName(requestVo.getCardNumber(), CardTypeEnum.getCardIndex(requestVo.getCardType()), requestVo.getName());
        if (passengers == null || passengers.size() == 0){
            return Result.setResult(ResultCodeEnum.MANAGE_WITHOUTPASSENGER_ERROR);
        }
        System.out.println(JSONObject.toJSONString(passengers));
        ArrayList<Long> passengerIds = new ArrayList<>();
        for (Passenger passenger: passengers) {
            passengerIds.add(passenger.getPassengerId());
        }
        List<OrderInfo> orderInfos = orderInfoService.findAllByPassengerIdInAndOrderStatus(passengerIds, OrderStatusEnum.STANDBY.getIndex());
        if (orderInfos == null || orderInfos.size() == 0){
            return Result.setResult(ResultCodeEnum.ORDER_GETORDERINFO_ERROR);
        }
        ArrayList<OrderInfoManageVo> orderInfoVos = new ArrayList<>();
        for (OrderInfo orderInfo: orderInfos) {
            ShuttleShift shuttleShift = shuttleShiftService.findByShiftId(orderInfo.getShuttleShiftId());
            if (shuttleShift == null){
                return Result.setResult(ResultCodeEnum.MANAGE_GETORDERLIST_ERROR);
            }
            ShuttleLine shuttleLine = shuttleService.findByLineId(shuttleShift.getShuttleLineId());
            if (shuttleLine == null){
                return Result.setResult(ResultCodeEnum.MANAGE_GETORDERLIST_ERROR);
            }
            OrderInfoManageVo orderInfoManageVo = new OrderInfoManageVo();
            orderInfoManageVo.setOrderId(orderInfo.getOrderId());
            orderInfoManageVo.setCreateTime(parseTime(orderInfo.getCreateTime()));
            orderInfoManageVo.setShuttleLine(shuttleLine.getStartRegion() + "-" + shuttleLine.getFinalRegion() + " " + shuttleShift.getShuttleShiftDate() + " " + shuttleShift.getShuttleShiftTime());
            orderInfoManageVo.setPassengerCardNumber(requestVo.getCardNumber());
            orderInfoManageVo.setPassengerCardType(requestVo.getCardType());
            orderInfoManageVo.setPassengerName(requestVo.getName());
            orderInfoManageVo.setRideCode(orderInfo.getRideCode());
            orderInfoVos.add(orderInfoManageVo);
        }
        return Result.ok().data("order_list",orderInfoVos);
    }

    @Override
    public ResultCodeEnum modifyOrderInfo(modifyOrderRequestVo requestVo) {
        ResultCodeEnum checkResultCode = checkModifyOrderInfoParams(requestVo);
        if (!checkResultCode.getSuccess()){
            return checkResultCode;
        }
        Passenger passenger = new Passenger();
        passenger.setName(requestVo.getName());
        passenger.setCardType(CardTypeEnum.getCardIndex(requestVo.getCardType()));
        passenger.setCardNumber(requestVo.getCardNumber());
        passenger.setIsDelete(Const.DELETE);
        passenger.setUserId(-1L);
        Passenger addResult = passengerService.addPassenger(passenger);
        if (addResult == null){
            return ResultCodeEnum.MANAGE_MODIFYORDERINFO_ERROR;
        }
        OrderInfo orderInfo = orderInfoService.findByOrderId(requestVo.getOrderId());
        if (orderInfo == null){
            return ResultCodeEnum.ORDER_GETORDERINFO_ERROR;
        }
        orderInfo.setPassengerId(addResult.getPassengerId());
        OrderInfo saveResult = orderInfoService.save(orderInfo);
        if (saveResult == null){
            return ResultCodeEnum.MANAGE_MODIFYORDERINFO_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }


    private ResultOfDataPage packShuttleShifts(Page<ShuttleShift> shuttleShifts, Integer page, Integer size, String startRegion, String finalRegion){
        ArrayList<ShuttleInfoManageVo> shuttleShiftVos = new ArrayList<>();
        for (ShuttleShift shuttleShift: shuttleShifts.getContent()) {
            ShuttleInfoManageVo shuttleShiftVo = new ShuttleInfoManageVo();
            BeanUtils.copyProperties(shuttleShift,shuttleShiftVo);
            shuttleShiftVo.setStatus(ShuttleShiftStatusEnum.getShuttleShiftStatusName(shuttleShift.getIsDelete()));
            shuttleShiftVo.setShuttleShiftType(ShuttleShiftTypeEnum.getShuttleShiftTypeName(shuttleShift.getShuttleShiftType()));
            shuttleShiftVo.setLineType(LineTypeEnum.getLineTypeName(shuttleShift.getLineType()));
            shuttleShiftVo.setStartRegion(startRegion);
            shuttleShiftVo.setFinalRegion(finalRegion);
            shuttleShiftVos.add(shuttleShiftVo);
        }
        return ResultOfDataPage.ok().data("shuttle_list", shuttleShiftVos)
                .setDataInfo(page,size,shuttleShifts.getTotalElements());
    }

    private ResultCodeEnum checkGetOrderListParams(getOrderRequestVo requestVo) {
        if (requestVo == null || requestVo.getName() == null ||
                requestVo.getCardType() == null ||
                requestVo.getCardNumber() == null ){
            return ResultCodeEnum.MANAGE_PARAMNULL_ERROR;
        }
        if (requestVo.getName().length() == 0 ||
                requestVo.getCardNumber().length() == 0 ||
                requestVo.getCardType().length() == 0){
            return ResultCodeEnum.MANAGE_PARAM_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    private ResultCodeEnum checkGetShuttleInfoListParams(GetShuttleInfoListRequestVo requestVo) {
        if (requestVo == null || requestVo.getStartRegionId() == null ||
                requestVo.getFinalRegionId() == null || requestVo.getShuttleShiftDate() == null ||
                requestVo.getSize() == null || requestVo.getPage() == null){
            return ResultCodeEnum.MANAGE_PARAMNULL_ERROR;
        }
        if (requestVo.getStartRegionId() <= 0 ||
                requestVo.getFinalRegionId() <= 0 ||
                requestVo.getShuttleShiftDate().length() == 0 ||
                requestVo.getPage() <= 0 || requestVo.getSize() <= 0){
            return ResultCodeEnum.MANAGE_PARAM_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    private ResultCodeEnum checkCreateShuttleInfoParams(ShuttleInfoManageVo requestVo) {
        if (requestVo == null || requestVo.getShuttleShiftDate() == null ||
                requestVo.getShuttleShiftTime() == null || requestVo.getStartRegion() == null ||
                requestVo.getFinalRegion() == null || requestVo.getStartStation() == null ||
                requestVo.getFinalStation() == null || requestVo.getTicketQuantity() == null ||
                requestVo.getChildTicketQuantity() == null || requestVo.getUnuseTicketQuantity() == null ||
                requestVo.getUnuseChildTicketQuantity() == null || requestVo.getTicketPrice() == null ||
                requestVo.getRefundFee() == null || requestVo.getStationFee() == null ||
                requestVo.getInsurancePrice() == null || requestVo.getShuttleShiftType() == null ||
                requestVo.getLineType() == null || requestVo.getDuration() == null ||
                requestVo.getFullLenght() == null || requestVo.getStatus() == null ||
                requestVo.getStationNumber() == null){
            return ResultCodeEnum.MANAGE_PARAMNULL_ERROR;
        }
        System.out.println("入参1：" + JSONObject.toJSONString(requestVo));
        if (requestVo.getShuttleShiftDate().length() == 0 ||
                requestVo.getShuttleShiftTime().length() == 0 || requestVo.getStartRegion().length() == 0 ||
                requestVo.getFinalRegion().length() == 0 || requestVo.getStartStation().length() == 0 ||
                requestVo.getFinalStation().length() == 0 || requestVo.getTicketQuantity() <0 ||
                requestVo.getChildTicketQuantity() <0 || requestVo.getUnuseTicketQuantity() <0 ||
                requestVo.getUnuseChildTicketQuantity() <0 || requestVo.getShuttleShiftType().length() == 0 ||
                requestVo.getLineType().length() == 0 || requestVo.getDuration().length() == 0 ||
                requestVo.getFullLenght().length() == 0 || requestVo.getStatus().length() == 0 ||
                requestVo.getStationNumber().length() == 0 ){
            return ResultCodeEnum.MANAGE_PARAM_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    private ResultCodeEnum checkModifyOrderInfoParams(modifyOrderRequestVo requestVo) {
        if (requestVo == null || requestVo.getName() == null ||
                requestVo.getCardType() == null ||
                requestVo.getCardNumber() == null ||
                requestVo.getOrderId() == null){
            return ResultCodeEnum.MANAGE_PARAMNULL_ERROR;
        }
        if (requestVo.getName().length() == 0 ||
                requestVo.getCardNumber().length() == 0 ||
                requestVo.getCardType().length() == 0 ||
                requestVo.getOrderId() <= 0L){
            return ResultCodeEnum.MANAGE_PARAM_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    private String parseTime(Long time){
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
