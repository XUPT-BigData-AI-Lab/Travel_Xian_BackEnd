package com.coach_station.bootmall.controller;

import com.coach_station.bootmall.entity.Station;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.service.QueryService;
import com.coach_station.bootmall.vo.OrderDetailInfoVo;
import com.coach_station.bootmall.vo.Result;
import com.coach_station.bootmall.vo.StationDetailInfoVo;
import com.coach_station.bootmall.vo.StationInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/14/15:34
 * @Description:
 */
@RequestMapping("/query/station")
@Controller
@CrossOrigin
public class QueryStationController {

    @Autowired
    QueryService queryService;

    // 获取常见车站列表
    @GetMapping("/getFamliarStation")
    @ResponseBody
    public Result getFamliarStations(@RequestParam(name = "size") Integer size) {
        List<StationInfoVo> stations = queryService.getFamliarStations(size);
        if (stations == null){
            return Result.setResult(ResultCodeEnum.QUERY_GETFAMLIARSTATIONS_ERROR);
        }
        return Result.ok().
                data("famliar_stations_list",stations);
    }

    // 所有车站详细信息及经纬度接口
    @GetMapping("/getAllStations")
    @ResponseBody
    public Result getAllStations() {
        List<StationDetailInfoVo> stations = queryService.getAllStations();
        if (stations == null){
            return Result.setResult(ResultCodeEnum.QUERY_GETALLSTATIONS_ERROR);
        }
        return Result.ok().
                data("station_list",stations);
    }
}
