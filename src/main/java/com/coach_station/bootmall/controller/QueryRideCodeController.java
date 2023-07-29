package com.coach_station.bootmall.controller;

import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import com.coach_station.bootmall.service.QueryService;
import com.coach_station.bootmall.vo.Result;
import com.coach_station.bootmall.vo.StationDetailInfoVo;
import com.coach_station.bootmall.vo.StationInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: yjw
 * @Date: 2022/01/14/15:34
 * @Description:
 */
@RequestMapping("/query/rideCode")
@Controller
@CrossOrigin
public class QueryRideCodeController {

    @Autowired
    QueryService queryService;

    // 首页发送乘车码至手机
    @GetMapping("/sendRideCode")
    @ResponseBody
    public Result sendRideCode(@RequestParam(name = "phone_number") String phoneNumber,
                               @RequestParam(name = "check_code") String checkCode) {
        return Result.setResult(queryService.sendRideCode(phoneNumber,checkCode));
    }
}
