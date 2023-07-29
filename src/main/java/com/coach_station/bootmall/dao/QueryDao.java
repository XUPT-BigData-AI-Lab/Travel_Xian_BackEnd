package com.coach_station.bootmall.dao;

import com.coach_station.bootmall.entity.Article;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.vo.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: yjw
 * @Date: 2022/01/14/15:45
 * @Description:
 */
public interface QueryDao {
    List<StationInfoVo> getFamliarStations(Integer size);

    List<StationDetailInfoVo> getAllStations();

    List<ShuttleVo> getFamliarShuttles(Integer size);

    List<RegionVo> getAllRegions();

    Map<String, Object> getShuttleList(Long startRegionId, Long finalRegionId, String shuttleShiftDate) throws ParseException;

    ResultCodeEnum sendRideCode(String phoneNumber, String checkCode);

    HashMap<String, Object> getArticleInfo(Long articleId);
}
