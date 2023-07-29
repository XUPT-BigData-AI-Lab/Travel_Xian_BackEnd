package com.coach_station.bootmall.service;

import com.alibaba.fastjson.JSONObject;
import com.coach_station.bootmall.dao.OrderDao;
import com.coach_station.bootmall.dto.OrderDetailInfoDto;
import com.coach_station.bootmall.dto.OrderInfoDto;
import com.coach_station.bootmall.entity.*;
import com.coach_station.bootmall.enumAndConst.*;
import com.coach_station.bootmall.util.GenerateNum;
import com.coach_station.bootmall.vo.*;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: yjw
 * @Date: 2022/01/12/14:22
 * @Description:
 */
@Service
@Log4j2
public class OrderService implements OrderDao {

    @Autowired
    OrderDtoService orderDtoService;

    @Autowired
    OrderDetailInfoService orderDetailInfoService;

    @Autowired
    OrderOperateService orderOperateService;

    @Autowired
    ShuttleShiftService shuttleShiftService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    PassengerService passengerService;

    @Autowired
    ShuttleService shuttleService;

    @Autowired
    ContactPersonService contactPersonService;

    @Autowired
    StationService stationService;

    @Autowired
    CarService carService;

    @Override
    public Map<String, Object> getOrderInfo(String startTime, String endTime, String orderStatus, Integer page, Integer size) throws ParseException {
        ResultCodeEnum checkCode = checkParams(orderStatus, page, size);
        if (!checkCode.getSuccess()){
            return null;
            //TODO 打日志
        }
        Long userId = (Long)getSessionAttribute("userId");
        if (startTime != null && endTime != null){
            ResultCodeEnum checkTimeCode = checkTimeParams(startTime,endTime);
            if (!checkTimeCode.getSuccess()){
                return null;
                //TODO 打日志
            }
            Long newStartTime = parseDateTime(startTime);
            Long newEndTime = parseDateTime(endTime);
            if (newEndTime.equals(0L) || newStartTime.equals(0L)){
                return null;
                //TODO 打日志
            }
            if (orderStatus.equals("all")){
                return findAllMasterOrdersByTime(userId, newStartTime, newEndTime, page, size);
            }
            if (orderStatus.equals("refund")){
                return findAllMasterRefundOrdersByTime(userId, newStartTime, newEndTime, page, size);
            }
            return findMasterOrdersByTimeAndOrderStatus(userId,
                    OrderStatusEnum.getOrderStatusIndexByEngName(orderStatus),
                    newStartTime,newEndTime,page,size);
        }
        if (orderStatus.equals("all")){
            return findAllMasterOrders(userId, page, size);
        }
        if (orderStatus.equals("refund")){
            return findRefundMasterOrders(userId, page, size);
        }
        return findMasterOrdersByOrderStatus(userId,
                OrderStatusEnum.getOrderStatusIndexByEngName(orderStatus),page,size);
    }

    @Override
    public List<OrderDetailInfoVo> getOrderDetailInfo(String masterOrderNumber) throws ParseException {
        ResultCodeEnum checkCode = checkParamsOrderNumber(masterOrderNumber);
        if (!checkCode.getSuccess()){
            return null;
            //TODO 打日志
        }
        Long userId = (Long)getSessionAttribute("userId");
        List<OrderDetailInfoDto> orderDetailInfo = orderDetailInfoService.findOrderDetailInfoByMasterOrderNumber(userId,
                Const.CHILD_ORDER,
                masterOrderNumber);
        if (orderDetailInfo == null){
            return null;
        }
        return packOrderDetailInfoVo(orderDetailInfo);
    }

    @Override
    public ResultCodeEnum refundOrder(String orderNumber, String refundType) {
//        ResultCodeEnum checkCode = checkParamsOrderNumber(masterOrderNumber);
//        if (!checkCode.getSuccess()){
//            return null;
//            //TODO 打日志
//        }
//        Long userId = (Long)getSessionAttribute("userId");
//        List<OrderDetailInfoDto> orderDetailInfo = orderDetailInfoService.findOrderDetailInfoByMasterOrderNumber(1L,
//                Const.CHILD_ORDER,
//                masterOrderNumber);
//        if (orderDetailInfo == null){
//            return null;
//        }
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultCodeEnum cancelOrder(String orderNumber) {
        Long userId = (Long) getSessionAttribute("userId");
        if (userId == null || userId <= 0 || orderNumber.length() < 22) {
            return ResultCodeEnum.ORDER_CANCELORDER_ERROR;
            //TODO 打日志
        }
        List<OrderInfo> orderInfos = orderInfoService.findByOrderNumberAndMasterOrderNumberAndUserId(orderNumber,userId);
        log.info("orderInfos的参数信息是：{}", orderInfos);
        if (orderInfos == null){
            return ResultCodeEnum.ORDER_CANCELORDER_ERROR;
            //TODO 打日志
        }
        List<OrderInfo> orderInfosResp = ModifyOrderStatus(orderInfos);
        if (orderInfosResp == null){
            return ResultCodeEnum.ORDER_CANCELORDER_ERROR;
        }
        List<OrderOperate> orderOperatesResp = saveOrderOperate(orderInfos);
        if (orderOperatesResp == null){
            return ResultCodeEnum.ORDER_CANCELORDER_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> bookOrder(BookOrderVo bookOrderVo) {
        Map<String,Object> result = new HashMap<>();
        ResultCodeEnum codeEnum = checkBookOrderVo(bookOrderVo);
        if (!codeEnum.getSuccess()){
            result.put("code",codeEnum);
            return result;
        }
        System.out.println("bookOrderVo:" + JSONObject.toJSONString(bookOrderVo));
        Long userId = (Long) getSessionAttribute("userId");
        List<OrderInfoDto> checkArrearageOrders = checkUserHaveArrearageOrder(userId);
        if (checkArrearageOrders != null && checkArrearageOrders.size() > 0){
            result.put("code",ResultCodeEnum.ORDER_BOOKORDER_HAVEARRANGEORDER_ERROR);
            return result;
        }

        Long personId = checkAndAddContactPerson(bookOrderVo, userId);
        if (personId == null){
            result.put("code",ResultCodeEnum.ORDER_BOOKORDER_ERROR);
            return result;
        }
        System.out.println("personId:" + JSONObject.toJSONString(personId));

        HashMap<String, Passenger> passengerIDs = checkAndAddPassenger(bookOrderVo, userId);
        if (passengerIDs == null){
            result.put("code",ResultCodeEnum.ORDER_BOOKORDER_ERROR);
            return result;
        }
        System.out.println("passengerIDs:" + JSONObject.toJSONString(passengerIDs));

        ShuttleShift shuttleShift = shuttleShiftService.findByShiftId(bookOrderVo.getShuttleShiftId());
        System.out.println("shuttleShift:" + JSONObject.toJSONString(shuttleShift));

        int haveChildTickectQuantity = 0;
        for (OrderPassengerVo orderPassengerVo:bookOrderVo.getPassengers()) {
            if (orderPassengerVo.getTicketType().equals(TicketTypeEnum.HAVECHILD.getName())){
                haveChildTickectQuantity++;
            }
        }
        if ((shuttleShift.getUnuseTicketQuantity() < bookOrderVo.getPassengers().size()) && (shuttleShift.getUnuseChildTicketQuantity() < haveChildTickectQuantity)){
            result.put("code",ResultCodeEnum.ORDER_BOOKORDER_ERROR);
            return result;
        }
        System.out.println("haveChildTickectQuantity:" + JSONObject.toJSONString(haveChildTickectQuantity));

        List<OrderInfo> orderInfos = packOrderInfos(bookOrderVo, userId, shuttleShift, personId, passengerIDs);
        System.out.println("orderInfos:" + JSONObject.toJSONString(orderInfos));

        //一锁二判三更新
        // 一锁：锁shuttle_shift班次表
        ShuttleShift lockedShuttleShift = shuttleShiftService.findByShiftIdAndLock(bookOrderVo.getShuttleShiftId());
        // 二判：判断shuttle_shift班次表的余票是否够
        if ((lockedShuttleShift.getUnuseTicketQuantity() <= 0) || (lockedShuttleShift.getUnuseTicketQuantity() < bookOrderVo.getPassengers().size()) || (lockedShuttleShift.getUnuseChildTicketQuantity() < haveChildTickectQuantity)){
            throw new RuntimeException();
//            result.put("code",ResultCodeEnum.ORDER_BOOKORDER_TICKETNOTENOUGH_ERROR);
//            return result;
        }
        System.out.println("lockedShuttleShift:" + JSONObject.toJSONString(lockedShuttleShift));

        List<OrderInfo> orderInfoResult = AddOrderInfos(shuttleShift, orderInfos);
        if (orderInfoResult == null){
            result.put("code",ResultCodeEnum.ORDER_BOOKORDER_ERROR);
            return result;
        }
        System.out.println("orderInfoResult:" + JSONObject.toJSONString(orderInfoResult));

        List<OrderOperate> orderOperatesResult = AddOrderOperates(orderInfoResult);
        if (orderOperatesResult == null){
            result.put("code",ResultCodeEnum.ORDER_BOOKORDER_ERROR);
            return result;
        }
        System.out.println("orderOperatesResult:" + JSONObject.toJSONString(orderOperatesResult));

        // 三更新：更新班次的余票
        ShuttleShift shuttleShiftResult = updateShuttleShift(lockedShuttleShift, passengerIDs);
        if (shuttleShiftResult == null){
            result.put("code",ResultCodeEnum.ORDER_BOOKORDER_TICKETNOTENOUGH_ERROR);
            return result;
        }
        System.out.println("shuttleShiftResult:" + JSONObject.toJSONString(shuttleShiftResult));

        ShuttleLine shuttleLine = shuttleService.findByLineId(shuttleShift.getShuttleLineId());
        BookOrderResultVo bookOrderResultVo = packBookResults(orderInfos,shuttleShift,shuttleLine,passengerIDs);
        if (bookOrderResultVo == null){
            result.put("code",ResultCodeEnum.ORDER_BOOKORDER_ERROR);
            return result;
        }
        result.put("result",bookOrderResultVo);
        result.put("code",ResultCodeEnum.SUCCESS);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultCodeEnum payOrderCallBack(JSONObject bodyJsonObject,String tradeno) {
        Long userId = Long.valueOf(bodyJsonObject.getString("userId"));
        BigDecimal money = new BigDecimal(bodyJsonObject.getString("money"));
        String orderNumber = bodyJsonObject.getString("orderNumber");
        List<OrderInfo> orderInfos = orderInfoService.findByOrderNumberAndMasterOrderNumberAndUserId(orderNumber, userId);
        System.out.println("orderInfos：" + JSONObject.toJSONString(orderInfos));
        if (orderInfos == null || orderInfos.size() <= 0){
            return ResultCodeEnum.ORDER_PAYORDERCALLBACK_TICKETNOTENOUGH_ERROR;
        }
        String rideCode = UUID.randomUUID().toString().substring(0, 6);
        while (true) {
            List<OrderInfo> infos = orderInfoService.findByRideCode(rideCode);
            System.out.println("infos：" + JSONObject.toJSONString(infos));
            System.out.println("rideCode：" + JSONObject.toJSONString(rideCode));
            if (infos == null || infos.size() == 0){
                break;
            }
            rideCode = UUID.randomUUID().toString().substring(0, 6);
        }
        ArrayList<OrderOperate> orderOperates = new ArrayList<>();

        for (OrderInfo orderInfo: orderInfos) {
            if (orderInfo.getOrderStatus().compareTo(OrderStatusEnum.STANDBY.getIndex()) >= 0){
                return ResultCodeEnum.ORDER_PAYORDERCALLBACK_TICKETNOTENOUGH_ERROR;
            }
            if (orderInfo.getOrderType().equals(Const.MASTER_ORDER) && !orderInfo.getTotalAmount().equals(money)){
                return ResultCodeEnum.ORDER_PAYORDERCALLBACK_TICKETNOTENOUGH_ERROR;
            }
            orderInfo.setTradingNumber(tradeno);
            orderInfo.setOrderStatus(OrderStatusEnum.STANDBY.getIndex());
            orderInfo.setRideCode(rideCode);

            OrderOperate orderOperate = new OrderOperate();
            orderOperate.setOrderNumber(orderInfo.getOrderNumber());
            orderOperate.setOrderStatus(OrderStatusEnum.STANDBY.getIndex());
            orderOperate.setOperateTime(System.currentTimeMillis());
            orderOperates.add(orderOperate);
        }

        List<OrderInfo> orderInfosResult = orderInfoService.saveAll(orderInfos);
        System.out.println("orderInfosResult：" + JSONObject.toJSONString(orderInfosResult));
        if (orderInfosResult == null || orderInfosResult.size() == 0){
            return ResultCodeEnum.ORDER_PAYORDERCALLBACK_TICKETNOTENOUGH_ERROR;
        }

        List<OrderOperate> orderOperatesResult = orderOperateService.saveAll(orderOperates);
        System.out.println("orderOperatesResult：" + JSONObject.toJSONString(orderOperatesResult));
        if (orderOperatesResult == null || orderOperatesResult.size() == 0){
            return ResultCodeEnum.ORDER_PAYORDERCALLBACK_TICKETNOTENOUGH_ERROR;
        }

        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public String getRideCode(String masterOrderNumber) {
        OrderInfo orderInfo = orderInfoService.findByOrderNumber(masterOrderNumber);
        if (orderInfo == null){
            return null;
        }
        return orderInfo.getRideCode();
    }

    @Override
    public Map<String, Object> payOrderVerify(String masterOrderNumber) {
        HashMap<String, Object> payOrderVo = new HashMap<>();
        if (masterOrderNumber.length() < 22){
            return null;
        }
        OrderInfo orderInfo = orderInfoService.findByOrderNumber(masterOrderNumber);
        System.out.println(JSONObject.toJSONString(orderInfo));
        if (orderInfo == null || !orderInfo.getOrderStatus().equals(OrderStatusEnum.STANDBY.getIndex())){
            return null;
        }
        OrderOperate operate = orderOperateService.findByOrderNumberAndOrderStatus(masterOrderNumber, OrderStatusEnum.STANDBY.getIndex());
        if (operate == null){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        payOrderVo.put("master_order_number",masterOrderNumber);
        payOrderVo.put("total_amount",orderInfo.getTotalAmount());
        payOrderVo.put("completion_time",formatter.format(operate.getOperateTime()));
        return payOrderVo;
    }

    private List<OrderInfoDto> checkUserHaveArrearageOrder(Long userId) {
        return orderDtoService.findOrdersByOrderStatus(userId, Const.MASTER_ORDER, OrderStatusEnum.ARREARAGE.getIndex());
    }

    private BookOrderResultVo packBookResults(List<OrderInfo> orderInfos, ShuttleShift shuttleShift, ShuttleLine shuttleLine, HashMap<String,Passenger> passengerIDs) {
        HashMap<Long, Passenger> passengers = new HashMap<>();
        for (Map.Entry<String,Passenger> passenger: passengerIDs.entrySet()) {
            passengers.put(passenger.getValue().getPassengerId(), passenger.getValue());
        }

        BookOrderResultVo bookOrderResultVo = new BookOrderResultVo();
        ArrayList<BookOrderDetailResultVo> bookOrderDetailResultVos = new ArrayList<>();
        for (OrderInfo orderInfo:orderInfos) {
            if (orderInfo.getOrderType().equals(Const.CHILD_ORDER)){
                Passenger passenger = passengers.get(orderInfo.getPassengerId());
                Car car = carService.findByCarId(shuttleShift.getCarId());
                BookOrderDetailResultVo bookOrderDetailResultVo = new BookOrderDetailResultVo();
                bookOrderDetailResultVo.setShuttleShiftTime(shuttleShift.getShuttleShiftDate().toString() + " " + shuttleShift.getShuttleShiftTime());
                bookOrderDetailResultVo.setStartRegion(shuttleLine.getStartRegion());
                bookOrderDetailResultVo.setFinalRegion(shuttleLine.getFinalRegion());
                bookOrderDetailResultVo.setStartStation(shuttleShift.getStartStation());
                bookOrderDetailResultVo.setFinalStation(shuttleShift.getFinalStation());
                bookOrderDetailResultVo.setTicketPrice(shuttleShift.getTicketPrice());
                bookOrderDetailResultVo.setStationFee(shuttleShift.getStationFee());
                bookOrderDetailResultVo.setInsurancePrice(shuttleShift.getInsurancePrice());
                bookOrderDetailResultVo.setTicketType(TicketTypeEnum.geticketTypeName(orderInfo.getTicketType()));
                bookOrderDetailResultVo.setShuttleShiftId(shuttleShift.getShiftId());
                bookOrderDetailResultVo.setPassengerName(passenger.getName());
                String cardNumber = passenger.getCardNumber();
                if (cardNumber.startsWith("?")){
                    cardNumber = Const.WITHOUT_EMAIL;
                }
                bookOrderDetailResultVo.setPassengerCardNumber(cardNumber);
                bookOrderDetailResultVo.setPassengerType(TicketTypeEnum.getPersonType(orderInfo.getTicketType()));
                bookOrderDetailResultVo.setCarModel(car.getCarModel());
                bookOrderDetailResultVo.setOrderNumber(orderInfo.getOrderNumber());
                bookOrderDetailResultVo.setTotalAmount(orderInfo.getTotalAmount());
                bookOrderDetailResultVos.add(bookOrderDetailResultVo);
            }
            if (orderInfo.getOrderType().equals(Const.MASTER_ORDER))
                bookOrderResultVo.setMasterOrderNumber(orderInfo.getOrderNumber());
                bookOrderResultVo.setMasterTotalAmount(orderInfo.getTotalAmount());
        }
        bookOrderResultVo.setOrderInfos(bookOrderDetailResultVos);
        return bookOrderResultVo;
    }

    private ResultCodeEnum checkBookOrderVo(BookOrderVo bookOrderVo) {
        if (bookOrderVo == null){
            return ResultCodeEnum.ORDER_BOOKORDER_BOOKINFONOTENOUGH_ERROR;
        }
        if (bookOrderVo.getShuttleShiftId() == null || bookOrderVo.getShuttleShiftId().equals(0L)){
            return ResultCodeEnum.ORDER_BOOKORDER_BOOKINFONOTENOUGH_ERROR;
        }
        if (bookOrderVo.getContactPersonPhoneNumber() == null || bookOrderVo.getContactPersonPhoneNumber().length() <= 0){
            return ResultCodeEnum.ORDER_BOOKORDER_BOOKINFONOTENOUGH_ERROR;
        }
        if (bookOrderVo.getContactPersonName() == null || bookOrderVo.getContactPersonName().length() <= 1){
            return ResultCodeEnum.ORDER_BOOKORDER_BOOKINFONOTENOUGH_ERROR;
        }
        if (bookOrderVo.getPassengers() == null || bookOrderVo.getPassengers().size() <= 0){
            return ResultCodeEnum.ORDER_BOOKORDER_BOOKINFONOTENOUGH_ERROR;
        }
        Long userId = (Long)getSessionAttribute("userId");
        if ( userId == null || userId <= 0){
            return ResultCodeEnum.ORDER_BOOKORDER_BOOKINFONOTENOUGH_ERROR;
            //todo 打日志
        }
        for (OrderPassengerVo orderPassengerVo:bookOrderVo.getPassengers()) {
            if (orderPassengerVo.getPassengerName() == null || orderPassengerVo.getPassengerName().length() <= 1){
                return ResultCodeEnum.ORDER_BOOKORDER_BOOKINFONOTENOUGH_ERROR;
            }
            if (orderPassengerVo.getTicketType() == null || TicketTypeEnum.geticketTypeIndex(orderPassengerVo.getTicketType()) == -1){
                return ResultCodeEnum.ORDER_BOOKORDER_BOOKINFONOTENOUGH_ERROR;
            }
            if (!orderPassengerVo.getPassengerCardNumber().equals("-")){
                return UserCenterService.checkCardTypeAndCardNumber(orderPassengerVo.getPassengerCardType(), orderPassengerVo.getPassengerCardNumber());
            }
        }
        return ResultCodeEnum.SUCCESS;
    }

    private Long checkAndAddContactPerson(BookOrderVo bookOrderVo, Long userId) {
        if (bookOrderVo.getContactPersonId() == null){
            ContactPerson contactPerson = new ContactPerson();
            contactPerson.setName(bookOrderVo.getContactPersonName());
            contactPerson.setPhoneNumber(bookOrderVo.getContactPersonPhoneNumber());
            contactPerson.setUserId(userId);
            contactPerson.setIsDelete(Const.DELETE);
            String email = bookOrderVo.getContactPersonEmail();
            if (email == null || email.length() == 0){
                email = Const.WITHOUT_EMAIL;
            }
            contactPerson.setEmail(email);
            ContactPerson contactPersonResult = contactPersonService.addContactPerson(contactPerson);
            if (contactPersonResult == null){
                return null;
            }
            return contactPersonResult.getPersonId();
        }
        return bookOrderVo.getContactPersonId();
    }

    private ShuttleShift updateShuttleShift(ShuttleShift lockedShuttleShift, HashMap<String, Passenger> passengerIDs) {
        lockedShuttleShift.setUnuseTicketQuantity((lockedShuttleShift.getUnuseTicketQuantity()) - (passengerIDs.size()));
        ShuttleShift shuttleShift = shuttleShiftService.addShuttleShift(lockedShuttleShift);
        if (shuttleShift == null){
            return null;
        }
        return shuttleShift;
    }

    private List<OrderOperate> AddOrderOperates(List<OrderInfo> orderInfoResult) {
        ArrayList<OrderOperate> orderOperates = new ArrayList<>();
        for (OrderInfo orderInfo:orderInfoResult) {
            OrderOperate orderOperate = new OrderOperate();
            orderOperate.setOrderNumber(orderInfo.getOrderNumber());
            orderOperate.setOrderStatus(orderInfo.getOrderStatus());
            orderOperate.setOperateTime(System.currentTimeMillis());
            orderOperates.add(orderOperate);
        }
        List<OrderOperate> orderOperatesResult = orderOperateService.saveAll(orderOperates);
        if (orderOperatesResult == null || orderOperatesResult.size() == 0){
            return null;
        }
        return orderOperatesResult;
    }

    private List<OrderInfo> AddOrderInfos(ShuttleShift shuttleShift,List<OrderInfo> orderInfos) {
        List<OrderInfo> orderInfoResult = orderInfoService.saveAll(orderInfos);
        if (orderInfoResult == null){
            return null;
        }
        System.out.println("orderInfoResult:" + JSONObject.toJSONString(orderInfoResult));
        //添加订单号码并保存
        String masterOrderNumber = "";
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        for (OrderInfo orderInfo:orderInfoResult) {
            String orderNumber = shuttleShift.getStationNumber() + year.substring(2) + "00000000".substring(orderInfo.getOrderId().toString().length()) + orderInfo.getOrderId() + GenerateNum.generateOrder().substring(0,3);
            System.out.println("orderNumber:" + orderNumber);
            orderInfo.setOrderNumber(orderNumber);
            if (orderInfo.getOrderType().equals(1)){
                masterOrderNumber = orderNumber;
            }
        }
        for (OrderInfo orderInfo:orderInfoResult) {
            if (orderInfo.getOrderType().equals(2))
                orderInfo.setMasterOrderNumber(masterOrderNumber);
        }
        List<OrderInfo> orderInfoResult1 = orderInfoService.saveAll(orderInfoResult);
        if (orderInfoResult1 == null || orderInfoResult1.size() == 0){
            return null;
        }

        return orderInfoResult1;
    }

    private List<OrderInfo> packOrderInfos(BookOrderVo bookOrderVo, Long userId, ShuttleShift shuttleShift, Long personId, HashMap<String, Passenger> passengerIDs) {
        ArrayList<OrderInfo> orderInfos = new ArrayList<>();

        BigDecimal totolaAmount=BigDecimal.valueOf(0L);
        for (OrderPassengerVo passenger: bookOrderVo.getPassengers()) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderType(Const.CHILD_ORDER);
            orderInfo.setOrderStatus(OrderStatusEnum.ARREARAGE.getIndex());
            orderInfo.setUserId(userId);
            orderInfo.setRideCode("-1");
            orderInfo.setTradingNumber("-1");
            orderInfo.setRefundAmount(BigDecimal.valueOf(0L));
            orderInfo.setIsDelete(0);
            orderInfo.setSlaveOrderQuantity(-1);
            orderInfo.setFromOnline(Const.FROM_ONLINE);
            orderInfo.setPassengerId(passengerIDs.get(passenger.getPassengerCardNumber()).getPassengerId());
            orderInfo.setPersonId(personId);
            orderInfo.setCreateTime(System.currentTimeMillis());
            orderInfo.setTicketType(TicketTypeEnum.geticketTypeIndex(passenger.getTicketType()));
            orderInfo.setInsuranceStatus(InsuranceStatusEnum.getInsuranceStatusIndex(passenger.isBuyingInsurance()));
            orderInfo.setShuttleShiftId(bookOrderVo.getShuttleShiftId());
            BigDecimal orderAmount = shuttleShift.getTicketPrice().add(shuttleShift.getStationFee());
            if (passenger.getTicketType().equals(TicketTypeEnum.HALF.getName())){
                orderAmount = shuttleShift.getTicketPrice().divide(new BigDecimal(2), 2, RoundingMode.HALF_UP).add(shuttleShift.getStationFee());
            }
            if (passenger.isBuyingInsurance()){
                orderAmount = orderAmount.add(shuttleShift.getInsurancePrice());
            }
            totolaAmount = totolaAmount.add(orderAmount);
            orderInfo.setTotalAmount(orderAmount);
            orderInfos.add(orderInfo);
        }

        OrderInfo masterOrderInfo = new OrderInfo();
        masterOrderInfo.setOrderType(Const.MASTER_ORDER);
        masterOrderInfo.setSlaveOrderQuantity(passengerIDs.size());
        masterOrderInfo.setOrderStatus(OrderStatusEnum.ARREARAGE.getIndex());
        masterOrderInfo.setUserId(userId);
        masterOrderInfo.setPassengerId(-1L);
        masterOrderInfo.setRideCode("-1");
        masterOrderInfo.setTradingNumber("-1");
        masterOrderInfo.setTicketType(-1);
        masterOrderInfo.setInsuranceStatus(-1);
        masterOrderInfo.setRefundAmount(BigDecimal.valueOf(0L));
        masterOrderInfo.setIsDelete(0);
        masterOrderInfo.setMasterOrderNumber("-1");
        masterOrderInfo.setFromOnline(Const.FROM_ONLINE);
        masterOrderInfo.setPersonId(personId);
        masterOrderInfo.setCreateTime(System.currentTimeMillis());
        masterOrderInfo.setShuttleShiftId(bookOrderVo.getShuttleShiftId());
        masterOrderInfo.setTotalAmount(totolaAmount);
        orderInfos.add(masterOrderInfo);

        return orderInfos;
    }

    private HashMap<String, Passenger> checkAndAddPassenger(BookOrderVo bookOrderVo, Long userId) {
        HashMap<String, Passenger> passengerIDs = new HashMap<>();
        ArrayList<Passenger> passengers = new ArrayList<>();
        for (OrderPassengerVo passengerVo: bookOrderVo.getPassengers()) {
            if (passengerVo.getPassengerId() != null){
                Passenger passenger = new Passenger();
                passenger.setCardNumber(passengerVo.getPassengerCardNumber());
                passenger.setName(passengerVo.getPassengerName());
                passenger.setUserId(userId);
                passenger.setPassengerId(passengerVo.getPassengerId());
                passenger.setCardType(CardTypeEnum.getCardIndex(passengerVo.getPassengerCardType()));
                passengerIDs.put(passengerVo.getPassengerCardNumber(),passenger);
            }else {
                if (passengerVo.getPassengerCardNumber().equals("-")){
                    String cardnumber = "?" + GenerateNum.generateOrder();
                    passengerVo.setPassengerCardNumber(cardnumber);
                }
                Passenger passenger = new Passenger();
                passenger.setCardNumber(passengerVo.getPassengerCardNumber());
                passenger.setName(passengerVo.getPassengerName());
                passenger.setUserId(userId);
                passenger.setIsDelete(Const.DELETE);
                passenger.setCardType(CardTypeEnum.getCardIndex(passengerVo.getPassengerCardType()));
                passengers.add(passenger);
            }
        }
        List<Passenger> passengersResult = passengerService.addPassengers(passengers);
        if (passengersResult == null){
            return null;
        }

        for (Passenger passenger: passengersResult) {
            passengerIDs.put(passenger.getCardNumber(),passenger);
        }
        return passengerIDs;
    }

    private List<OrderOperate> saveOrderOperate(List<OrderInfo> orderInfos) {
        ArrayList<OrderOperate> orderOperates = new ArrayList<>();
        for (OrderInfo orderInfo: orderInfos) {
            orderOperates.add(new OrderOperate(
                    orderInfo.getOrderNumber(),
                    OrderStatusEnum.CANCEL.getIndex(),
                    System.currentTimeMillis()
            ));
        }
        List<OrderOperate> orderOperatesResp = orderOperateService.saveAll(orderOperates);
        if (orderOperatesResp == null || orderOperatesResp.size() == 0){
            return null;
        }
        return orderOperatesResp;
    }

    private List<OrderInfo> ModifyOrderStatus(List<OrderInfo> orderInfos) {
        if (orderInfos == null || orderInfos.size() == 0){
            return null;
        }
        for (OrderInfo orderInfo:orderInfos) {
            if (orderInfo.getOrderStatus() > OrderStatusEnum.ARREARAGE.getIndex())
                return null;
            orderInfo.setOrderStatus(OrderStatusEnum.CANCEL.getIndex());
        }
        List<OrderInfo> orderInfosResp = orderInfoService.saveAll(orderInfos);
        log.info("修改结果orderInfos的参数信息是：{}", orderInfosResp);
        if (orderInfosResp == null || orderInfosResp.size() == 0){
            return null;
        }
        return orderInfosResp;
    }

    public Map<String, Object> findAllMasterOrdersByTime(Long userId, Long newStartTime, Long newEndTime, Integer page, Integer size) throws ParseException {
        Map<String, Object> orderInfo = orderDtoService.findMasterOrdersByTime(userId,
                Const.MASTER_ORDER,
                newStartTime,
                newEndTime + Const.ONEDAY,
                page,
                size);
        if (orderInfo == null){
            return null;
        }
        return packOrderInfoVo(orderInfo);
    }

    public Map<String, Object> findAllMasterRefundOrdersByTime(Long userId, Long newStartTime, Long newEndTime, Integer page, Integer size) throws ParseException {
        Map<String, Object> orderInfo = orderDtoService.findRefundMasterOrdersByTime(userId,
                Const.MASTER_ORDER,
                newStartTime,
                newEndTime + Const.ONEDAY,
                page,
                size,
                Const.REFUND_ALL,
                Const.REFUND_PART);
        if (orderInfo == null){
            return null;
        }
        return packOrderInfoVo(orderInfo);
    }

    public Map<String, Object> findMasterOrdersByTimeAndOrderStatus(Long userId, Integer orderStaus, Long newStartTime, Long newEndTime, Integer page, Integer size) throws ParseException {
        Map<String, Object> orderInfo = orderDtoService.findMasterOrdersByTimeAndOrderStatus(userId,
                Const.MASTER_ORDER,
                newStartTime,
                newEndTime + Const.ONEDAY,
                page,
                size,
                orderStaus);
        if (orderInfo == null){
            return null;
        }
        return packOrderInfoVo(orderInfo);
    }

    public Map<String, Object> findAllMasterOrders(Long userId, Integer page, Integer size) throws ParseException {
        Map<String, Object> orderInfo = orderDtoService.findAllMasterOrders(userId,
                Const.MASTER_ORDER,
                page,
                size);
        if (orderInfo == null){
            return null;
        }
        return packOrderInfoVo(orderInfo);
    }

    public Map<String, Object> findRefundMasterOrders(Long userId, Integer page, Integer size) throws ParseException {
        Map<String, Object> orderInfo = orderDtoService.findRefundOrders(userId,
                Const.MASTER_ORDER,
                page,
                size,
                Const.REFUND_ALL,
                Const.REFUND_PART);
        if (orderInfo == null){
            return null;
        }
        return packOrderInfoVo(orderInfo);
    }

    public Map<String, Object> findMasterOrdersByOrderStatus(Long userId, Integer orderStatus, Integer page, Integer size) throws ParseException {
        Map<String, Object> orderInfo = orderDtoService.findMasterOrdersByOrderStatus(userId,
                Const.MASTER_ORDER,
                orderStatus,
                page,
                size);
        if (orderInfo == null){
            return null;
        }
        return packOrderInfoVo(orderInfo);
    }

    private Map<String, Object> packOrderInfoVo(Map<String, Object> orderInfo) {
        ArrayList<OrderInfoVo> orderInfoVos = new ArrayList<>();
        ArrayList<OrderInfoDto> orderInfoDtos = (ArrayList<OrderInfoDto>) orderInfo.get("pageOrderInfos");
        for (OrderInfoDto orderInfoDto: orderInfoDtos) {
            orderInfoVos.add(new OrderInfoVo(
                    orderInfoDto.getOrderId(),
                    orderInfoDto.getOrderNumber(),
                    orderInfoDto.getTotalAmount(),
                    orderInfoDto.getShuttleShiftDate().toString() + " " + orderInfoDto.getShuttleShiftTime(),
                    OrderStatusEnum.getOrderStatusName(orderInfoDto.getOrderStatus()),
                    orderInfoDto.getStartRegion(),
                    orderInfoDto.getFinalRegion()
            ));
        }
        orderInfo.put("OrderInfoVos",orderInfoVos);
        return orderInfo;
    }

    private ArrayList<OrderDetailInfoVo> packOrderDetailInfoVo(List<OrderDetailInfoDto> orderdetailInfo) {
        ArrayList<OrderDetailInfoVo> orderDetailInfoVos = new ArrayList<>();
        for (OrderDetailInfoDto orderDetailInfoDto: orderdetailInfo) {
            List<OrderOperate> orderOperates = orderOperateService.findByOrderNumber(orderDetailInfoDto.getOrderNumber());
            if (orderOperates == null){
                return null;
            }
            orderDetailInfoVos.add(new OrderDetailInfoVo(
                    orderDetailInfoDto.getOrderId(),
                    orderDetailInfoDto.getOrderNumber(),
                    orderDetailInfoDto.getTotalAmount(),
                    orderDetailInfoDto.getShuttleShiftDate().toString() + " " + orderDetailInfoDto.getShuttleShiftTime(),
                    OrderStatusEnum.getOrderStatusName(orderDetailInfoDto.getOrderStatus()),
                    orderDetailInfoDto.getStartRegion(),
                    orderDetailInfoDto.getFinalRegion(),
                    TicketTypeEnum.geticketTypeName(orderDetailInfoDto.getTicketType()),
                    CardTypeEnum.getCardName(orderDetailInfoDto.getCardType()),
                    orderDetailInfoDto.getCardNumber(),
                    orderDetailInfoDto.getName(),
                    orderDetailInfoDto.getRefundAmount(),
                    orderDetailInfoDto.getRideCode(),
                    orderDetailInfoDto.getStartStation(),
                    orderDetailInfoDto.getFinalStation(),
                    orderDetailInfoDto.getTicketPrice(),
                    orderDetailInfoDto.getStationFee(),
                    orderDetailInfoDto.getRefundFee(),
                    orderDetailInfoDto.getInsurancePrice(),
                    InsuranceStatusEnum.getInsuranceStatusName(orderDetailInfoDto.getInsuranceStatus()),
                    ShuttleShiftTypeEnum.getShuttleShiftTypeName(orderDetailInfoDto.getShuttleShiftType()),
                    LineTypeEnum.getLineTypeName(orderDetailInfoDto.getLineType()),
                    orderDetailInfoDto.getCarModel(),
                    orderDetailInfoDto.getShiftId(),
                    packOrderOperate(orderOperates)
            ));
        }
        return orderDetailInfoVos;
    }

    private String packOrderOperate(List<OrderOperate> orderOperates) {
        StringBuilder orderOperate = new StringBuilder();
        for (OrderOperate operate: orderOperates) {
            orderOperate.append(OperateOrderStatusEnum.getOperateOrderStatusName(operate.getOrderStatus())).
                    append(parseTime(operate.getOperateTime())).append(",");
        }
        return orderOperate.substring(0,orderOperate.length()-1);
    }

    private String parseTime(Long time){
        Date date = new Date(Long.parseLong(time + Const.TIME_PARSE));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    private Long parseDateTime(String dateTime) throws ParseException {
//        Long startTs = System.currentTimeMillis();
//        System.out.println("现在时间：" + startTs);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateTime).getTime();
//        String timestamp = String.valueOf(dateTimeStamp);
//        int length = timestamp.length();
//        if (length > 3) {
//            return Long.valueOf(timestamp.substring(0,length-3));
//        } else {
//            return 0L;
//        }
    }

    private ResultCodeEnum checkParams(String orderStatus, Integer page, Integer size) {
        if (page == null || page <= 0 || size == null || size <= 0){
            return ResultCodeEnum.ORDER_GETORDERINFO_ERROR;
        }
        if (OrderStatusEnum.getOrderStatusIndexByEngName(orderStatus) == -1) {
            System.out.println("检查");
            return ResultCodeEnum.ORDER_GETORDERINFO_ERROR;
        }

        Long userId = (Long)getSessionAttribute("userId");
        if ( userId == null || userId <= 0 ){
            return ResultCodeEnum.ORDER_GETORDERINFO_ERROR;
            //todo 打日志
        }
        return ResultCodeEnum.SUCCESS;
    }

    private ResultCodeEnum checkParamsOrderNumber(String masterOrderNumber) {
        if (masterOrderNumber == null || masterOrderNumber.length() < 22 ){
            return ResultCodeEnum.ORDER_GETORDERDETAILINFO_ERROR;
        }
        Long userId = (Long)getSessionAttribute("userId");
        if ( userId == null || userId <= 0 ){
            return ResultCodeEnum.ORDER_GETORDERDETAILINFO_ERROR;
            //todo 打日志
        }
        return ResultCodeEnum.SUCCESS;
    }


    private ResultCodeEnum checkTimeParams(String startTime, String endTime) {
        if (startTime == null || startTime.length() < 10 || endTime == null || endTime.length() < 10){
            return ResultCodeEnum.ORDER_GETORDERINFO_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    private Object getSessionAttribute(String attributeKey){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return session.getAttribute(attributeKey);
    }

    public static void main(String[] args) throws ParseException {
        Long startTs = System.currentTimeMillis();
        System.out.println(startTs);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        if (((System.currentTimeMillis() + 1800000) < sdf.parse("2022-04-24 11:50").getTime())){
            System.out.println("成功");
        }
        System.out.println(sdf.parse("2022-04-24 11:00").getTime());
        System.out.println(sdf.parse("2022-04-24 11:30").getTime());
        System.out.println(sdf.parse("2022-04-24 11:30").getTime() - sdf.parse("2022-04-24 11:00").getTime());
//        String timestamp = String.valueOf(dateTimeStamp);
//        int length = timestamp.length();
//        if (length > 3) {
//            return Long.valueOf(timestamp.substring(0,length-3));
//        } else {
//            return 0L;
//        }
    }
}
