package com.coach_station.bootmall.controller;

import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.service.QueryService;
import com.coach_station.bootmall.vo.RegionVo;
import com.coach_station.bootmall.vo.Result;
import com.coach_station.bootmall.vo.ShuttleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/15/15:28
 * @Description:
 */
@RequestMapping("/query/region")
@Controller
@CrossOrigin
public class RegionController {

    @Autowired
    QueryService queryService;

    // 获取所有地区列表接口
    @GetMapping("/getAllRegions")
    @ResponseBody
    public Result getAllRegions() {
        List<RegionVo> regions = queryService.getAllRegions();
        if (regions == null){
            return Result.setResult(ResultCodeEnum.QUERY_GETALLREGINONS_ERROR);
        }
        return Result.ok().
                data("region_list",regions);
    }
}
