package com.coach_station.bootmall.controller;

import com.coach_station.bootmall.configuration.AlipayConfig;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.service.QueryService;
import com.coach_station.bootmall.vo.Result;
import com.coach_station.bootmall.vo.ShuttleShiftInfoVo;
import com.coach_station.bootmall.vo.ShuttleVo;
import com.coach_station.bootmall.vo.StationInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: yjw
 * @Date: 2022/01/14/23:51
 * @Description:
 */
@RequestMapping("/query/shuttle")
@Controller
@CrossOrigin
public class QueryShuttleController {

    @Autowired
    QueryService queryService;

    @Autowired
    AlipayConfig alipayConfig;

    // 获取热门线路列表
    @GetMapping("/getFamliarShuttles")
    @ResponseBody
    public Result getFamliarShuttles(@RequestParam(name = "size") Integer size) {
        List<ShuttleVo> shuttles = queryService.getFamliarShuttles(size);
        if (shuttles == null){
            return Result.setResult(ResultCodeEnum.QUERY_GETFAMLIARSHUTTLES_ERROR);
        }
        return Result.ok().
                data("famliar_shuttle_list",shuttles);
    }

    // 查询满足线路条件的班次列表接口
    @GetMapping("/getShuttleList")
    @ResponseBody
    public Result getShuttleList(
            @RequestParam(name = "start_region_id") Long startRegionId,
            @RequestParam(name = "final_region_id") Long finalRegionId,
            @RequestParam(name = "shuttle_shift_date") String shuttleShiftDate) throws ParseException {
        Map<String, Object> shiftInfoVos = queryService.getShuttleList(startRegionId, finalRegionId, shuttleShiftDate);
        if (shiftInfoVos == null){
            return Result.setResult(ResultCodeEnum.QUERY_GETFAMLIARSHUTTLES_ERROR);
        }
        return Result.ok().
                data(shiftInfoVos);
    }
}
