package com.coach_station.bootmall.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.coach_station.bootmall.dao.QueryDao;
import com.coach_station.bootmall.dao.ShuttleDao;
import com.coach_station.bootmall.entity.*;
import com.coach_station.bootmall.enumAndConst.*;
import com.coach_station.bootmall.util.AliSMSUtil;
import com.coach_station.bootmall.vo.*;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: yjw
 * @Date: 2022/01/14/15:46
 * @Description:
 */
@Log4j2
@Service
public class QueryService implements QueryDao {

    @Autowired
    StationService stationService;

    @Autowired
    ShuttleService shuttleService;

    @Autowired
    RegionService regionService;

    @Autowired
    ShuttleShiftService shuttleShiftService;

    @Autowired
    CarService carService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    UserNewService userNewService;

    @Autowired
    ArticleService articleService;

    @Autowired
    RelationLineService relationLineService;

    @Override
    public List<StationDetailInfoVo> getAllStations() {
        List<Station> stations = stationService.findAll();
        return packStationsResult(stations);
    }

    @Override
    public List<ShuttleVo> getFamliarShuttles(Integer size) {
        if (size == null || size <= 0){
            return null;
            //todo 打日志
        }

        Long userId = (Long)getSessionAttribute("userId");
        if (userId == null){
            return getFamliarShuttleLines(size);
        }
        return getRelationShuttleLines(size,userId);
    }

    public List<ShuttleVo> getFamliarShuttleLines(Integer size) {
        Sort sort = new Sort(Sort.Direction.ASC,"startRegionId");
        Pageable pageable = PageRequest.of(0,size,sort);
        return packShuttlesResult(shuttleService.findAll(pageable).getContent());
    }

    public List<ShuttleVo> getRelationShuttleLines(Integer size, Long userId) {
        Sort sort = new Sort(Sort.Direction.DESC,"orderId");
        Pageable pageable = PageRequest.of(0,2,sort);
        Page<OrderInfo> twoOrderInfos = orderInfoService.findByUserId(userId, pageable);
        HashSet<Long> shuttleShiftIds = new HashSet<>();
        ArrayList<Long> shuttleLineIds = new ArrayList<>();
        for (OrderInfo orderInfo:twoOrderInfos) {
            shuttleShiftIds.add(orderInfo.getShuttleShiftId());
        }
        List<ShuttleShift> shuttleShifts = shuttleShiftService.findByShiftIdIn(shuttleShiftIds);
        for (ShuttleShift shuttleShift:shuttleShifts) {
            if (!shuttleLineIds.contains(shuttleShift.getShuttleLineId())) {
                shuttleLineIds.add(shuttleShift.getShuttleLineId());
            }
        }
        ArrayList<RelationLine> lines = new ArrayList<>();
        ArrayList<Long> lines1 = new ArrayList<>();
        if (shuttleLineIds.size() == 1){
            lines.addAll(relationLineService.findAllByFrontLine(shuttleLineIds.get(0) + ";"));
            lines.sort(new Comparator<RelationLine>() {
                @Override
                public int compare(RelationLine o1, RelationLine o2) {
                    return Integer.compare(o1.getConfidence().compareTo(o2.getConfidence()), 0);
                }
            });
            for (int i = lines.size()-1; i >= 0; i--) {
                String[] lineIds = lines.get(i).getEndLine().split(";");
                for (String lineId: lineIds) {
                    if (!lines1.contains(Long.valueOf(lineId))){
                        lines1.add(Long.valueOf(lineId));
                    }
                }
            }
        }else if (shuttleLineIds.size() == 2){
            lines.addAll(relationLineService.findAllByFrontLine(shuttleLineIds.get(0) + ";"));
            lines.addAll(relationLineService.findAllByFrontLine(shuttleLineIds.get(1) + ";"));
            lines.addAll(relationLineService.findAllByFrontLine(shuttleLineIds.get(0) + ";" + shuttleLineIds.get(1) + ";"));
            lines.addAll(relationLineService.findAllByFrontLine(shuttleLineIds.get(1) + ";" + shuttleLineIds.get(0) + ";"));
            lines.sort(new Comparator<RelationLine>() {
                @Override
                public int compare(RelationLine o1, RelationLine o2) {
                    return Integer.compare(o1.getConfidence().compareTo(o2.getConfidence()), 0);
                }
            });
            for (int i = lines.size()-1; i >= 0; i--) {
                String[] lineIds = lines.get(i).getEndLine().split(";");
                for (String lineId: lineIds) {
                    if (!lines1.contains(Long.valueOf(lineId))){
                        lines1.add(Long.valueOf(lineId));
                    }
                }
            }
        }
        ArrayList<ShuttleLine> shuttleLines2 = new ArrayList<>();
        List<ShuttleLine> shuttleLines1 = shuttleService.findAllByLineIdIn(lines1);
        for (Long lineId:lines1) {
            for (ShuttleLine shuttleLine:shuttleLines1) {
                if (shuttleLine.getLineId().equals(lineId)){
                    shuttleLines2.add(shuttleLine);
                }
            }
        }
        if (shuttleLines1.size() < 20){
            List<ShuttleLine> shuttleLines = shuttleService.findAll(
                    PageRequest.of(0, 20,
                            new Sort(Sort.Direction.DESC, "totalStars"))
            ).getContent();
            for (ShuttleLine shuttleLine:shuttleLines) {
                if (!lines1.contains(shuttleLine.getLineId())){
                    shuttleLines2.add(shuttleLine);
                }
                if (shuttleLines2.size() == 20){
                    break;
                }
            }
        }
        return packShuttlesResult(shuttleLines2);    }

    @Override
    public List<RegionVo> getAllRegions() {
        return packRegionsResult(regionService.findAll());
    }

    @Override
    public List<StationInfoVo> getFamliarStations(Integer size) {
        if (size == null || size <= 0){
            return null;
            //todo 打日志
        }
        Sort sort = new Sort(Sort.Direction.ASC,"stationId");
        Pageable pageable = PageRequest.of(0,size,sort);
        return packFamliarStationsResult(stationService.findByStar(1, pageable).getContent());
    }

    @Override
    public Map<String, Object> getShuttleList(Long startRegionId, Long finalRegionId, String shuttleShiftDate) throws ParseException {
        ResultCodeEnum code = checkParamsOfGetShuttleList(startRegionId, finalRegionId, shuttleShiftDate);
        if (!code.getSuccess()){
            return null;
        }
        ShuttleLine shuttleLine = shuttleService.findByStartRegionIdAndAndFinalRegionId(startRegionId, finalRegionId);
//        if (shuttleLine == null || shuttleLine.getLineId().equals(0L) || shuttleLine.getViaRegionsName().length() == 0){
        if (shuttleLine == null || shuttleLine.getLineId().equals(0L)){
            return null;
        }
//        Date date = parseDate(shuttleShiftDate);
        List<ShuttleShift> shuttleShifts = shuttleShiftService.findByShuttleLineIdAndShuttleShiftDate(shuttleLine.getLineId(), shuttleShiftDate);
        return packShuttleShiftsResult(shuttleShifts, shuttleLine.getStartRegion(), shuttleLine.getFinalRegion(), shuttleLine.getViaRegionsName());
    }

    @Override
    public ResultCodeEnum sendRideCode(String phoneNumber, String checkCode) {
        if (phoneNumber == null || phoneNumber.length() < 11 || checkCode == null || checkCode.length() < 4){
            return ResultCodeEnum.PARAM_ERROR;
        }
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String imgcode = (String) session.getAttribute("imgcode");
        if (imgcode == null){
            return ResultCodeEnum.LOGIN_CAPATA_ERROR;
        }
        if (checkCode.toUpperCase().equals(imgcode)){
            User user = userNewService.findByPhoneNumber(phoneNumber);
            if (user == null){
                return ResultCodeEnum.QUERY_PHONENUMBERNOTEXIST_ERROR;
            }
            List<OrderInfo> orderInfos = orderInfoService.findAllByUserIdAndAndOrderStatusAndOrderTypeAndIsDelete(
                    user.getUserId(),
                    OrderStatusEnum.STANDBY.getIndex(),
                    Const.MASTER_ORDER,
                    Const.NOT_DELETE
            );
            if (orderInfos == null || orderInfos.size() <= 0){
                return ResultCodeEnum.QUERY_NOTEXISTRIDECODE_ERROR;
            }
            for (OrderInfo orderInfo: orderInfos) {
                SendSmsResponse sendResp;
                try {
                    sendResp = AliSMSUtil.sendSMS(phoneNumber, orderInfo.getRideCode(), Const.RIDE_TEMPLATE_CODE);
                }catch (Exception e) {
                    log.error("异常信息: " + e);
                    return ResultCodeEnum.SEND_SMS_ERROR;
                }
                if (sendResp == null
                        || sendResp.body == null
                        || (!com.aliyun.teautil.Common.equalString(sendResp.body.code, "OK"))) {
                    log.error("错误信息: " + sendResp.body.message + "");
                    return ResultCodeEnum.SEND_SMS_ERROR;
                }
                return ResultCodeEnum.SUCCESS;
            }
        }else {
            return ResultCodeEnum.LOGIN_CAPATA_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    @Override
    public HashMap<String, Object> getArticleInfo(Long articleId) {
        if (articleId == null || articleId <= 0){
            return null;
        }
        Article article = articleService.findByArticleId(articleId);
        if (article == null){
            return null;
        }
        HashMap<String, Object> articleVo = new HashMap<>();
        articleVo.put("title",article.getTitle());
        articleVo.put("content",article.getContent());
        return articleVo;
    }

    private ResultCodeEnum checkParamsOfGetShuttleList(Long startRegionId, Long finalRegionId, String shuttleShiftDate) throws ParseException {
        if (startRegionId == null || startRegionId.equals(0L) || finalRegionId == null || finalRegionId.equals(0L) || shuttleShiftDate == null || shuttleShiftDate.length() <= 0){
            return ResultCodeEnum.QUERY_GETSHUTTLELIST_ERROR;
        }
        return ResultCodeEnum.SUCCESS;
    }

    private java.util.Date parseDate(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateTime);
    }

    private Map<String, Object> packShuttleShiftsResult(List<ShuttleShift> shuttleShifts, String startRegion, String finalRegion, String viaStationsName) throws ParseException {
        if (shuttleShifts == null || shuttleShifts.size() == 0){
            return null;
        }
        ArrayList<ShuttleShiftInfoVo> regularShuttleShiftInfoVos = new ArrayList<>();
        ArrayList<ShuttleShiftInfoVo> flowShuttleShiftInfoVos = new ArrayList<>();
        for (ShuttleShift shuttleShift:shuttleShifts) {
            Car car = carService.findByCarId(shuttleShift.getCarId());
            if (car == null || car.getCarId().equals(0L)){
                return null;
            }
            ShuttleShiftInfoVo shuttleShiftInfoVo = new ShuttleShiftInfoVo(
                    shuttleShift.getShiftId(),
                    shuttleShift.getShuttleShiftDate().toString() + " " + shuttleShift.getShuttleShiftTime(),
                    startRegion,
                    finalRegion,
                    shuttleShift.getStartStation(),
                    shuttleShift.getFinalStation(),
                    shuttleShift.getUnuseTicketQuantity(),
                    shuttleShift.getUnuseChildTicketQuantity(),
                    shuttleShift.getTicketPrice(),
                    ShuttleShiftTypeEnum.getShuttleShiftTypeName(shuttleShift.getShuttleShiftType()),
                    LineTypeEnum.getLineTypeName(shuttleShift.getLineType()),
                    shuttleShift.getDuration(),
                    shuttleShift.getFullLenght(),
                    car.getCarModel(),
                    viaStationsName
            );
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            if (((System.currentTimeMillis() + 1800000) < sdf.parse(shuttleShiftInfoVo.getShuttleShiftTime()).getTime()) && (shuttleShift.getShuttleShiftType().equals(ShuttleShiftTypeEnum.FIXED.getIndex()))){
                regularShuttleShiftInfoVos.add(shuttleShiftInfoVo);
            }
            if (((System.currentTimeMillis() + 1800000) < sdf.parse(shuttleShiftInfoVo.getShuttleShiftTime()).getTime()) && (shuttleShift.getShuttleShiftType().equals(ShuttleShiftTypeEnum.FLOW.getIndex()))){
                flowShuttleShiftInfoVos.add(shuttleShiftInfoVo);
            }
        }
        HashMap<String, Object> shuttleShiftInfoVoMap = new HashMap<>();
        shuttleShiftInfoVoMap.put("flow_shuttle_list",flowShuttleShiftInfoVos);
        shuttleShiftInfoVoMap.put("regular_shuttle_list",regularShuttleShiftInfoVos);
        return shuttleShiftInfoVoMap;
    }

    private List<StationInfoVo> packFamliarStationsResult(List<Station> stations){
        if (stations == null || stations.size() == 0){
            return null;
        }
        ArrayList<StationInfoVo> stationInfoVos = new ArrayList<>();
        for (Station station:stations) {
            stationInfoVos.add(new StationInfoVo(
                    station.getStationId(),
                    station.getStationName(),
                    station.getStationEnglishName()
            ));
        }
        return stationInfoVos;
    }

    private List<ShuttleVo> packShuttlesResult(List<ShuttleLine> shuttles){
        if (shuttles == null || shuttles.size() == 0){
            return null;
        }
        ArrayList<ShuttleVo> shuttleVos = new ArrayList<>();
        for (ShuttleLine shuttle:shuttles) {
            shuttleVos.add(new ShuttleVo(
                    shuttle.getStartRegionId(),
                    shuttle.getFinalRegionId(),
                    shuttle.getStartRegion(),
                    shuttle.getFinalRegion()
            ));
        }
        return shuttleVos;
    }

    private List<RegionVo> packRegionsResult(List<Region> regions){
        if (regions == null || regions.size() == 0){
            return null;
        }
        ArrayList<RegionVo> regionVos = new ArrayList<>();
        for (Region region:regions) {
            regionVos.add(new RegionVo(
                    region.getRegionnId(),
                    region.getRegionName(),
                    region.getRegionEnglishName(),
                    region.getCityName(),
                    region.getProvinceName()
            ));
        }
        return regionVos;
    }

    private List<StationDetailInfoVo> packStationsResult(List<Station> stations) {
        if (stations == null || stations.size() == 0){
            return null;
        }
        ArrayList<StationDetailInfoVo> stationDetailInfoVos = new ArrayList<>();
        for (Station station:stations) {
            stationDetailInfoVos.add(new StationDetailInfoVo(
                    station.getStationId(),
                    station.getStationName(),
                    station.getStationEnglishName(),
                    station.getLongitude(),
                    station.getLatitude(),
                    station.getStationAddress()
            ));
        }
        return stationDetailInfoVos;
    }

    private Object getSessionAttribute(String attributeKey){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return session.getAttribute(attributeKey);
    }
}
